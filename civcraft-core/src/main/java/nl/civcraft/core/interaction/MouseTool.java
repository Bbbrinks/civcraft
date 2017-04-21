package nl.civcraft.core.interaction;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface MouseTool {
    void handleLeftClick(boolean isPressed);

    void handleMouseMotion(float xDiff,
                           float yDiff);

    String getLabel();
}
