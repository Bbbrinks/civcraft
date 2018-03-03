package nl.civcraft.opengl.interaction;

import nl.civcraft.core.interaction.MouseToolManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Bob on 3-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class ToolManager {
    private final MouseInputManager mouseInputManager;
    private final MouseToolManager mouseToolManager;

    @Inject
    public ToolManager(MouseInputManager mouseInputManager,
                       MouseToolManager mouseToolManager) {
        this.mouseInputManager = mouseInputManager;
        this.mouseToolManager = mouseToolManager;

        mouseInputManager.registerMovementListener(vector2f -> mouseToolManager.getSelectedMouseTool().handleMouseMotion(vector2f.x, vector2f.y));
        mouseInputManager.registerLeftClickListener(isClicked -> mouseToolManager.getSelectedMouseTool().handleLeftClick(isClicked));
    }
}
