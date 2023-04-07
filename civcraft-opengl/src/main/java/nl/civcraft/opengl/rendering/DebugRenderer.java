package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.engine.GameEngine;
import nl.civcraft.opengl.interaction.KeyboardInputManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.AABBf;

import javax.inject.Inject;
import javax.inject.Named;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;

/**
 * Created by Bob on 3-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class DebugRenderer {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Node rootNode;
    private final Node debugNode;
    private boolean active = false;
    private long oldTime;

    @Inject
    public DebugRenderer(@Named("debugNode") Node debugNode,
                         @Named("rootNode") Node rootNode,

                         KeyboardInputManager keyboardInputManager,
                         GameEngine gameEngine) {
        this.rootNode = rootNode;
        this.debugNode = debugNode;

        keyboardInputManager.bindKey(GLFW_KEY_F10, this::activate);
        gameEngine.getUpdateScene().subscribe(this::debug);
    }


    private void activate(Float aFloat) {
        active = !active;
        AABBf boundingBox = rootNode.getBoundingBox();
        LOGGER.info(boundingBox);
    }


    private void debug(Float aFloat) {
        debugNode.detachAll();
        if(active){
            long newTime = System.nanoTime();
            LOGGER.info("fps: " + (1.0f / ((newTime - oldTime) / Math.pow(10, 9))));
            oldTime = newTime;
        }
    }
}
