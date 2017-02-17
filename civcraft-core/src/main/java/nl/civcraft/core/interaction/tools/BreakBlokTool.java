package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.tasks.BreakBlockTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class BreakBlokTool extends SingleVoxelSelector {

    private final TaskManager taskManager;

    @Autowired
    public BreakBlokTool(CurrentVoxelHighlighter currentVoxelHighlighter, TaskManager taskManager) {
        super(currentVoxelHighlighter);
        this.taskManager = taskManager;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (isPressed) {
            taskManager.addTask(new BreakBlockTask(currentVoxel));
        }
    }

    @Override
    public String getLabel() {
        return "Break block";
    }
}