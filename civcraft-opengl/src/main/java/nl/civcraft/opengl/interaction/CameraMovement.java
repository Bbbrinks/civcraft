package nl.civcraft.opengl.interaction;

import nl.civcraft.core.interaction.MouseInputManagerInterface;
import nl.civcraft.opengl.rendering.Camera;
import org.joml.Vector2f;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class CameraMovement {

    private static final float MOVEMENT_SPEED = 6.0f;
    private final MouseInputManagerInterface mouseInput;
    private final Camera camera;

    @Inject
    public CameraMovement(KeyboardInputManager keyboardInputManager,
                          MouseInputManagerInterface mouseInput,
                          Camera camera) {
        keyboardInputManager.bindKey(GLFW_KEY_A, tpf -> camera.movePosition(-MOVEMENT_SPEED * tpf, 0, 0));
        keyboardInputManager.bindKey(GLFW_KEY_D, tpf -> camera.movePosition(MOVEMENT_SPEED * tpf, 0, 0));
        keyboardInputManager.bindKey(GLFW_KEY_W, tpf -> camera.movePosition(0, 0, -MOVEMENT_SPEED * tpf));
        keyboardInputManager.bindKey(GLFW_KEY_S, tpf -> camera.movePosition(0, 0, MOVEMENT_SPEED * tpf));

        keyboardInputManager.bindKey(GLFW_KEY_SPACE, tpf -> camera.movePosition(0, MOVEMENT_SPEED * tpf, 0));
        keyboardInputManager.bindKey(GLFW_KEY_LEFT_SHIFT, tpf -> camera.movePosition(tpf, -MOVEMENT_SPEED * tpf, 0));

        this.mouseInput = mouseInput;
        this.camera =camera;
    }

    public void handleMouseMovement(Vector2f mouseMovement) {
        if (mouseInput.isRightButtonPressed()) {
            camera.moveRotation(mouseMovement.x, mouseMovement.y, 0);
        }
    }
}
