package nl.civcraft.opengl.rendering;


import nl.civcraft.opengl.engine.ShaderProgram;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.rendering.light.DirectionalLight;
import nl.civcraft.opengl.rendering.light.PointLight;
import nl.civcraft.opengl.rendering.light.SceneLight;
import nl.civcraft.opengl.rendering.light.SpotLight;
import nl.civcraft.opengl.util.MatrixUtil;
import nl.civcraft.opengl.util.ResourceUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class Renderer {

    private static final int MAX_POINT_LIGHTS = 5;

    private static final int MAX_SPOT_LIGHTS = 5;

    private ShadowMap shadowMap;

    private ShaderProgram depthShaderProgram;

    private ShaderProgram sceneShaderProgram;

    private final float specularPower;

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Camera camera;

    @Inject
    public Renderer(Camera camera) {
        this.camera = camera;
        specularPower = 10f;
    }

    public void init(Window window) throws Exception {
        shadowMap = new ShadowMap();

        setupDepthShader();
        setupSceneShader();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void render(Window window,
                       Scene scene,
                       Node debugNode) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        finalizeNode(scene.getRootNode());

        DirectionalLight light = scene.getSceneLight().getDirectionalLight();
        Vector3f lightDirection = light.getDirection();

        float lightAngleX = (float) Math.toDegrees(Math.acos(lightDirection.z));
        float lightAngleY = (float) Math.toDegrees(Math.asin(lightDirection.x));
        float lightAngleZ = 0;

        Matrix4f lightViewMatrix = MatrixUtil.getLightViewMatrix(new Vector3f(lightDirection).mul(light.getShadowPosMult()), new Vector3f(lightAngleX, lightAngleY, lightAngleZ));
        DirectionalLight.OrthoCoords orthCoords = light.getOrthoCoords();
        Matrix4f orthoProjMatrix = MatrixUtil.getOrthoProjectionMatrix(orthCoords.left, orthCoords.right, orthCoords.bottom, orthCoords.top, orthCoords.near, orthCoords.far);

        // Render depth map before view ports has been set up
        // TODO: something with this to make shadows
        renderDepthMap(window, camera, scene, lightViewMatrix, orthoProjMatrix);

        glViewport(0, 0, window.getWidth(), window.getHeight());


        renderScene(window, camera, scene, lightViewMatrix, orthoProjMatrix);
    }

    private void renderScene(Window window,
                            Camera camera,
                            Scene scene,
                            Matrix4f lightViewMatrix,
                            Matrix4f orthoProjMatrix) {
        sceneShaderProgram.bind();

        Matrix4f projectionMatrix = MatrixUtil.getProjectionMatrix(window);
        sceneShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        sceneShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);
        Matrix4f viewMatrix = MatrixUtil.getViewMatrix(camera);

        SceneLight sceneLight = scene.getSceneLight();
        renderLights(viewMatrix, sceneLight);

        sceneShaderProgram.setUniform("fog", scene.getFog());
        sceneShaderProgram.setUniform("texture_sampler", 0);
        sceneShaderProgram.setUniform("normalMap", 1);
        sceneShaderProgram.setUniform("shadowMap", 2);
        
        doRender(scene, sceneShaderProgram, viewMatrix, lightViewMatrix);

        sceneShaderProgram.unbind();
    }


    private void finalizeNode(Node node) {
        node.getChildren().forEachRemaining(this::finalizeNode);
        node.recalculateBoundingBox();
    }

    private void renderNode(Node currentNode,
                            ShaderProgram shader,
                            Matrix4f viewMatrix,
                            Matrix4f lightViewMatrix) {
        // TODO: check culling and shit
        currentNode.getChildren().forEachRemaining(node -> renderNode(node, shader, viewMatrix, lightViewMatrix));

        Matrix4f nodeTransform = currentNode.getTransform();

        // Set world matrix for this item

        if (viewMatrix != null) {
            Matrix4f modelViewMatrix = viewMatrix.mul(nodeTransform, new Matrix4f());
            sceneShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        }
        Matrix4f modelLightViewMatrix = lightViewMatrix.mul(nodeTransform, new Matrix4f());
        sceneShaderProgram.setUniform("modelLightViewMatrix", modelLightViewMatrix);

        currentNode.getGeometries().forEachRemaining(geometry -> geometry.render(viewMatrix, shader, shadowMap));
    }

    private void setupDepthShader() throws Exception {
        depthShaderProgram = new ShaderProgram();
        depthShaderProgram.createVertexShader(ResourceUtil.readResource("shaders/depth_vertex.vs"));
        depthShaderProgram.createFragmentShader(ResourceUtil.readResource("shaders/depth_fragment.fs"));
        depthShaderProgram.link();

        depthShaderProgram.createUniform("modelLightViewMatrix");
        depthShaderProgram.createUniform("orthoProjectionMatrix");
    }


    private void setupSceneShader() throws Exception {
        // Create shader
        sceneShaderProgram = new ShaderProgram();
        sceneShaderProgram.createVertexShader(ResourceUtil.readResource("shaders/scene_vertex.vs"));
        sceneShaderProgram.createFragmentShader(ResourceUtil.readResource("shaders/scene_fragment.fs"));
        sceneShaderProgram.link();

        // Create uniforms for modelView and projection matrices
        sceneShaderProgram.createUniform("projectionMatrix");
        sceneShaderProgram.createUniform("modelViewMatrix");
        sceneShaderProgram.createUniform("texture_sampler");
        sceneShaderProgram.createUniform("normalMap");
        // Create uniform for material
        sceneShaderProgram.createMaterialUniform("material");
        // Create lighting related uniforms
        sceneShaderProgram.createUniform("specularPower");
        sceneShaderProgram.createUniform("ambientLight");
        sceneShaderProgram.createPointLightListUniform("pointLights", MAX_POINT_LIGHTS);
        sceneShaderProgram.createSpotLightListUniform("spotLights", MAX_SPOT_LIGHTS);
        sceneShaderProgram.createDirectionalLightUniform("directionalLight");
        sceneShaderProgram.createFogUniform("fog");

        // Create uniforms for shadow mapping
        sceneShaderProgram.createUniform("shadowMap");
        sceneShaderProgram.createUniform("orthoProjectionMatrix");
        sceneShaderProgram.createUniform("modelLightViewMatrix");

    }

    private void renderDepthMap(Window window,
                                Camera camera,
                                Scene scene,
                                Matrix4f lightViewMatrix,
                                Matrix4f orthoProjMatrix) {

        // Setup view port to match the texture size
        glBindFramebuffer(GL_FRAMEBUFFER, shadowMap.getDepthMapFBO());
        glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);

        depthShaderProgram.bind();

        depthShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);

        doRender(scene, depthShaderProgram, null, lightViewMatrix);

        // Unbind
        depthShaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    private void doRender(Scene scene,
                          ShaderProgram shader,
                          Matrix4f viewMatrix,
                          Matrix4f lightViewMatrix) {
        renderNode(scene.getRootNode(), shader, viewMatrix, lightViewMatrix);
    }
    

    private void renderLights(Matrix4f viewMatrix,
                              SceneLight sceneLight) {

        sceneShaderProgram.setUniform("ambientLight", sceneLight.getAmbientLight());
        sceneShaderProgram.setUniform("specularPower", specularPower);

        // Process Point Lights
        PointLight[] pointLightList = sceneLight.getPointLightList();
        int numLights = pointLightList != null ? pointLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList[i]);
            Vector3f lightPos = currPointLight.getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            sceneShaderProgram.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        SpotLight[] spotLightList = sceneLight.getSpotLightList();
        numLights = spotLightList != null ? spotLightList.length : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList[i]);
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));

            Vector3f lightPos = currSpotLight.getPointLight().getPosition();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            sceneShaderProgram.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(sceneLight.getDirectionalLight());
        Vector4f dir = new Vector4f(currDirLight.getDirection(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3f(dir.x, dir.y, dir.z));
        sceneShaderProgram.setUniform("directionalLight", currDirLight);
    }

    public void cleanup() {
        if (shadowMap != null) {
            shadowMap.cleanup();
        }
        if (depthShaderProgram != null) {
            depthShaderProgram.cleanup();
        }
        if (sceneShaderProgram != null) {
            sceneShaderProgram.cleanup();
        }
    }

    public Vector3f getOrigin() {
        return camera.getPosition();
    }
}
