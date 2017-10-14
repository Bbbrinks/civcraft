package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.tasks.Harvest;

import javax.inject.Inject;
import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class HarvestTool extends SingleVoxelSelector {

    private final TaskManager taskManager;

    @Inject
    public HarvestTool(CurrentVoxelHighlighter currentVoxelHighlighter,
                       TaskManager taskManager) {
        super(currentVoxelHighlighter);
        this.taskManager = taskManager;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        Optional<Harvestable> component = currentVoxel.getComponent(Harvestable.class);
        component.ifPresent(harvestable -> taskManager.addTask(new Harvest(currentVoxel)));
    }

    @Override
    public String getLabel() {
        return "Harvest";
    }
}
