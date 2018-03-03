package nl.civcraft.opengl.interaction;

import nl.civcraft.core.interaction.MouseInputManagerInterface;
import nl.civcraft.opengl.rendering.Camera;

import javax.inject.Inject;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CameraMovement {

    private static final float MOVEMENT_SPEED = 6.0f;

    @Inject
    public CameraMovement(KeyboardInputManager keyboardInputManager,
                          MouseInputManagerInterface mousseInput,
                          Camera camera) {
        keyboardInputManager.bindKey(GLFW_KEY_A, tpf -> camera.movePosition(-MOVEMENT_SPEED * tpf, 0, 0));
        keyboardInputManager.bindKey(GLFW_KEY_D, tpf -> camera.movePosition(MOVEMENT_SPEED * tpf, 0, 0));
        keyboardInputManager.bindKey(GLFW_KEY_W, tpf -> camera.movePosition(0, 0, -MOVEMENT_SPEED * tpf));
        keyboardInputManager.bindKey(GLFW_KEY_S, tpf -> camera.movePosition(0, 0, MOVEMENT_SPEED * tpf));

        keyboardInputManager.bindKey(GLFW_KEY_SPACE, tpf -> camera.movePosition(0, MOVEMENT_SPEED * tpf, 0));
        keyboardInputManager.bindKey(GLFW_KEY_LEFT_SHIFT, tpf -> camera.movePosition(tpf, -MOVEMENT_SPEED * tpf, 0));

        mousseInput.registerMovementListener(mouseMovement -> {
            if (mousseInput.isRightButtonPressed()) {
                camera.moveRotation(mouseMovement.x, mouseMovement.y, 0);
            }
        });
    }
}
