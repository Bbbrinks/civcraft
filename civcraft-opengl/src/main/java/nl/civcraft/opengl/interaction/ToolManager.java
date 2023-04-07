package nl.civcraft.opengl.interaction;

import nl.civcraft.core.interaction.MouseToolManager;
import org.joml.Vector2f;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Bob on 3-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class ToolManager {
    private final MouseToolManager mouseToolManager;

    @Inject
    public ToolManager(MouseToolManager mouseToolManager) {
        this.mouseToolManager = mouseToolManager;
    }

    public void handleMovement(Vector2f mouseMovement) {
        mouseToolManager.getSelectedMouseTool().handleMouseMotion(mouseMovement.x, mouseMovement.y);
    }

    public void handleMouseClick() {
        mouseToolManager.getSelectedMouseTool().handleLeftClick();
    }
}
