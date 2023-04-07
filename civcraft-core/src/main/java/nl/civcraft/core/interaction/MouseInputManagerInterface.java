package nl.civcraft.core.interaction;

import org.joml.Vector2d;
import org.joml.Vector2f;

import java.util.function.Consumer;

/**
 * Created by Bob on 3-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public interface MouseInputManagerInterface {
    void registerMovementListener(Consumer<Vector2f> listener);

    void registerLeftClickListener(Runnable listener);

    boolean isRightButtonPressed();

    Vector2d getMousePosition();
}
