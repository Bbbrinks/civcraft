package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.pathfinding.AStarPathFinder;
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
    private final AStarPathFinder pathFinder;

    @Autowired
    public BreakBlokTool(CurrentVoxelHighlighter currentVoxelHighlighter, TaskManager taskManager, AStarPathFinder pathFinder) {
        super(currentVoxelHighlighter);
        this.taskManager = taskManager;
        this.pathFinder = pathFinder;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (isPressed) {
            taskManager.addTask(new BreakBlockTask(currentVoxel, pathFinder));
        }
    }

    @Override
    public String getLabel() {
        return "Break block";
    }
}
