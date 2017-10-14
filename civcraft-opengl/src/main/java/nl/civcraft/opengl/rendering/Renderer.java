package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.engine.ShaderProgram;
import nl.civcraft.opengl.engine.Transformation;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.util.ResourceUtil;
import org.joml.Matrix4f;

import javax.inject.Inject;

import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Renderer {

    /**
     * Field of View in Radians
     */
    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private final Transformation transformation;

    private ShaderProgram shaderProgram;
    private final Camera camera;

    @Inject
    public Renderer(Camera camera) {
        this.camera = camera;
        transformation = new Transformation();
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
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void render(Window window, Node rootNode) {
        clear();

        if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        shaderProgram.bind();

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

        shaderProgram.setUniform("texture_sampler", 0);

        renderNode(rootNode);

        shaderProgram.unbind();
    }

    private void renderNode(Node currentNode) {
        for (Node node : currentNode.getChildren()) {
            // TODO: check culling and shit
            renderNode(node);
        }

        Matrix4f nodeTransform = currentNode.getTransform();

        // Set world matrix for this item
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(nodeTransform, transformation.getViewMatrix(camera));
        shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

        for (Supplier<Geometry> mesh : currentNode.getGeometries()) {
            mesh.get().render();
        }
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
