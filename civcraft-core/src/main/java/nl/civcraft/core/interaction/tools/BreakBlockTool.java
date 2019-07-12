package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.tasks.BreakBlockTask;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BreakBlockTool extends SingleVoxelSelector {

    private final TaskManager taskManager;

    @Inject
    public BreakBlockTool(MousePicker mousePicker,
                          TaskManager taskManager,
                          @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        super(mousePicker, voxelHighlightManager);
        this.taskManager = taskManager;
    }

    @Override
    public void handleLeftClick() {
        taskManager.addTask(new BreakBlockTask(currentVoxel));
    }

    @Override
    public String getLabel() {
        return "Break block";
    }
}
