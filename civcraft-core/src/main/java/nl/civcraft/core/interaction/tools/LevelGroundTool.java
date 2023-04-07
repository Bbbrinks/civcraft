package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;
import nl.civcraft.core.tasks.BreakBlockTask;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class LevelGroundTool extends GroundRectangleSelector {

    private final TaskManager taskManager;
    private List<BreakBlockTask> tasks;


    @Inject
    public LevelGroundTool(                           VoxelManager voxelManager,
                           TaskManager taskManager,
                           @Named("voxelHighlight") PrefabManager voxelHighlightManager,
                           MousePicker mousePicker) {
        super(mousePicker, voxelManager, voxelHighlightManager);
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
        while (Neighbour.hasNeighbour(currentGround, NeighbourDirection.BOTTOM) && currentGround.getTransform().getTranslation(new Vector3f()).y() >
                startingVoxel.getTransform().getTranslation(new Vector3f()).y()) {
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
