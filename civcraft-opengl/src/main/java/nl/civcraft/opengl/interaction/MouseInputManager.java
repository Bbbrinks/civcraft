package nl.civcraft.opengl.interaction;

import nl.civcraft.opengl.engine.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.*;
@Singleton
public class MouseInputManager implements nl.civcraft.core.interaction.MouseInputManagerInterface {

    private final Vector2d previousPos;

    private final Vector2d currentPos;

    private final Vector2f displVec;

    private boolean inWindow = false;

    private boolean rightButtonPressed = false;

    private final List<Consumer<Vector2f>> movementsListeners;
    private final List<Runnable>  leftClickListeners;

    public MouseInputManager() {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        displVec = new Vector2f();
        movementsListeners = new ArrayList<>();
        leftClickListeners = new ArrayList<>();
    }

    public void init(Window window) {
        glfwSetCursorPosCallback(window.getWindowHandle(), (windowHandle, xpos, ypos) -> {
            currentPos.x = xpos;
            currentPos.y = ypos;
        });
        glfwSetCursorEnterCallback(window.getWindowHandle(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandle(), (windowHandle, button, action, mode) -> {
            if(button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                leftClickListeners.forEach(Runnable::run);
            }
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
        });
    }

    @Override
    public void registerMovementListener(Consumer<Vector2f> listener) {
        this.movementsListeners.add(listener);
    }

    @Override
    public void registerLeftClickListener(Runnable listener) {
        this.leftClickListeners.add(listener);
    }


    public void input(Window window) {
        displVec.x = 0;
        displVec.y = 0;
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            double deltax = currentPos.x - previousPos.x;
            double deltay = currentPos.y - previousPos.y;
            boolean rotateX = deltax != 0;
            boolean rotateY = deltay != 0;
            if (rotateX) {
                displVec.y = (float) deltax;
            }
            if (rotateY) {
                displVec.x = (float) deltay;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
        movementsListeners.forEach(listener -> listener.accept(displVec));

    }

    @Override
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    @Override
    public Vector2d getMousePosition() {
        return currentPos;
    }
}
