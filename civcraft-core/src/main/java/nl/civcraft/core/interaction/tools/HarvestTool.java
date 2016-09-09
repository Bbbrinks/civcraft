package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.EntityManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Harvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class HarvestTool extends SingleVoxelSelector {

    private final TaskManager taskManager;
    private final AStarPathFinder pathFinder;
    private final EntityManager entityManager;

    @Autowired
    public HarvestTool(CurrentVoxelHighlighter currentVoxelHighlighter, TaskManager taskManager, AStarPathFinder pathFinder, EntityManager entityManager) {
        super(currentVoxelHighlighter);
        this.taskManager = taskManager;
        this.pathFinder = pathFinder;
        this.entityManager = entityManager;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        Optional<Harvestable> component = currentVoxel.getComponent(Harvestable.class);
        if (component.isPresent()) {
            taskManager.addTask(new Harvest(entityManager, currentVoxel, pathFinder));
        }
    }
}
