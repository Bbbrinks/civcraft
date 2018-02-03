package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.engine.GameEngine;
import nl.civcraft.opengl.interaction.KeyboardInputManager;
import nl.civcraft.opengl.raycast.MousePicker;
import nl.civcraft.opengl.rendering.material.Texture;
import nl.civcraft.opengl.rendering.material.TextureManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.AABBf;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;

/**
 * Created by Bob on 3-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class DebugRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Node rootNode;
    private final Node debugNode;
    private final TextureManager textureManager;
    private final MousePicker mousePicker;
    private final Camera camera;
    private boolean active = false;
    private Texture texture;
    private Box box;
    private Node rayNode;

    @Inject
    public DebugRenderer(@Named("debugNode") Node debugNode,
                         @Named("rootNode") Node rootNode,
                         TextureManager textureManager,
                         KeyboardInputManager keyboardInputManager,
                         GameEngine gameEngine,
                         MousePicker mousePicker,
                         Camera camera) {
        this.rootNode = rootNode;
        this.debugNode = debugNode;
        this.textureManager = textureManager;
        this.mousePicker = mousePicker;
        this.camera = camera;

        keyboardInputManager.bindKey(GLFW_KEY_F10, this::activate);
        keyboardInputManager.bindKey(GLFW_KEY_F11, this::drawRay);
        gameEngine.getUpdateScene().subscribe(this::debug);
    }

    private void drawRay(Float aFloat) {
        if (active) {
            this.rayNode = new Node("ray", null);
            LOGGER.info("Look along " + mousePicker.getMouseDirection());
            this.rayNode.getTransform().lookAlong(mousePicker.getMouseDirection(), new Vector3f(0, 1, 0));
            this.rayNode.getTransform().setTranslation(new Vector3f(camera.getPosition()));
            this.rayNode.getTransform().scale(0.01f, 0.01f, 1000f);
            this.rayNode.addChild(new Geometry(Collections.singletonList(box), texture));
        }
    }

    private void activate(Float aFloat) {
        if (this.texture == null) {
            this.texture = textureManager.loadTexture("/textures/blue.png");
            this.box = Box.instance();
        }
        active = !active;
        AABBf boundingBox = rootNode.getBoundingBox();
        LOGGER.info(boundingBox);
    }


    private void debug(Float aFloat) {
        debugNode.detachAll();
        if (active) {
            Node rootBoundingBoxNode = new Node("rootBoundingBox", debugNode);
            AABBf boundingBox = rootNode.getBoundingBox();
            rootBoundingBoxNode.getTransform().translate((boundingBox.maxX - boundingBox.minX) / 2.0f, (boundingBox.maxY - boundingBox.minY) / 2.0f, (boundingBox.maxZ - boundingBox.minZ) / 2.0f);
            rootBoundingBoxNode.getTransform().scale(boundingBox.maxX - boundingBox.minX, boundingBox.maxY - boundingBox.minY, boundingBox.maxZ - boundingBox.minZ);
            rootBoundingBoxNode.addChild(new Geometry(Collections.singletonList(box), texture));
            if (this.rayNode != null) {
                debugNode.addChild(this.rayNode);
            }
        }
    }
}
