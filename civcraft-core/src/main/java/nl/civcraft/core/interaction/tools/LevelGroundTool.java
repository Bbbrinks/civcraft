package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;
import nl.civcraft.core.tasks.BreakBlockTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class LevelGroundTool extends GroundRectangleSelector {

    private final TaskManager taskManager;
    private List<BreakBlockTask> tasks;

    @Autowired
    public LevelGroundTool(CurrentVoxelHighlighter currentVoxelHighlighter,
                           VoxelManager voxelManager,
                           TaskManager taskManager,
                           @Qualifier("voxelHighlight") PrefabManager voxelHighlightManager) {
        super(currentVoxelHighlighter, voxelManager, voxelHighlightManager);
        this.taskManager = taskManager;
    }

    @Override
    protected void startSelection() {
        tasks = new ArrayList<>();
    }

    @Override
    protected void endSelection() {
        for (BreakBlockTask task : tasks) {
            taskManager.addTask(task);
        }
        tasks = null;
    }

    @Override
    protected void handleSelection(GameObject voxel) {
        GameObject currentGround = voxel;
        while (Neighbour.hasNeighbour(currentGround, NeighbourDirection.BOTTOM) && currentGround.getTransform().getTranslation().getY() > startingVoxel.getTransform().getTranslation().getY()) {
            tasks.add(new BreakBlockTask(currentGround));
            Optional<GameObject> neighbour = Neighbour.getNeighbour(currentGround, NeighbourDirection.BOTTOM);
            if (neighbour.isPresent()) {
                currentGround = neighbour.get();
            } else {
                throw new IllegalStateException("Could not find bottom neighbour");
            }
        }
    }

    @Override
    public String getLabel() {
        return "Level ground";
    }
}
