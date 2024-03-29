package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.tasks.Harvest;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class HarvestTool extends SingleVoxelSelector {

    private final TaskManager taskManager;

    @Inject
    public HarvestTool(MousePicker mousePicker,
                       TaskManager taskManager,
                       @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        super(mousePicker, voxelHighlightManager);
        this.taskManager = taskManager;
    }

    @Override
    public void handleLeftClick() {
        Optional<Harvestable> component = currentVoxel.getComponent(Harvestable.class);
        component.ifPresent(harvestable -> taskManager.addTask(new Harvest(currentVoxel)));
    }

    @Override
    public String getLabel() {
        return "Harvest";
    }
}
