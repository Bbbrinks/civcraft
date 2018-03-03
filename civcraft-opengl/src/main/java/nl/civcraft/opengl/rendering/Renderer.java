package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.engine.ShaderProgram;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.util.MatrixUtil;
import nl.civcraft.opengl.util.ResourceUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private ShaderProgram shaderProgram;
    private final Camera camera;

    @Inject
    public Renderer(Camera camera) {
        this.camera = camera;
    }

    public void init(Window window) throws Exception {
        // Create shader
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(ResourceUtil.readResource("vertex.vs"));
        shaderProgram.createFragmentShader(ResourceUtil.readResource("fragment.fs"));
        shaderProgram.link();

        // Create uniforms for world and projection matrices and texture
        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public void render(Window window,
                       Node rootNode,
                       Node debugNode) {
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        renderScene(window, rootNode, debugNode);
    }

    private void renderScene(Window window,
                            Node rootNode,
                            Node debugNode) {
        finalizeNode(rootNode);

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = MatrixUtil.getProjectionMatrix(window);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        shaderProgram.setUniform("texture_sampler", 0);

        Matrix4f viewMatrix = MatrixUtil.getViewMatrix(camera);
        renderNode(rootNode, viewMatrix);
        renderNode(debugNode, viewMatrix);

        shaderProgram.unbind();
    }


    private void finalizeNode(Node node) {
        node.getChildren().forEachRemaining(this::finalizeNode);
        node.recalculateBoundingBox();
    }

    private void renderNode(Node currentNode,
                            Matrix4f viewMatrix) {
        // TODO: check culling and shit
        currentNode.getChildren().forEachRemaining(node -> renderNode(node, viewMatrix));

        Matrix4f nodeTransform = currentNode.getTransform();

        // Set world matrix for this item
        Matrix4f modelViewMatrix = viewMatrix.mul(nodeTransform, new Matrix4f());
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

        currentNode.getGeometries().forEachRemaining(Geometry::render);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }

    public Vector3f getOrigin() {
        return camera.getPosition();
    }
}
