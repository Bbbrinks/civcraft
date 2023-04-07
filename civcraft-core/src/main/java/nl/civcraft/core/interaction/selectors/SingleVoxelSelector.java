package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Named("singleVoxelSelector")
public class SingleVoxelSelector implements MouseTool {

    private final MousePicker mousePicker;
    private final PrefabManager voxelHighlightManager;
    protected GameObject currentVoxel;

    @Inject
    public SingleVoxelSelector(MousePicker mousePicker,
                               @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        this.mousePicker = mousePicker;
        this.voxelHighlightManager = voxelHighlightManager;
    }

    @Override
    public void handleLeftClick() {
        //noop
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {
        voxelHighlightManager.destroyAll();
        mousePicker.pickNearest(Voxel.class).ifPresent(gameObject -> {
            this.currentVoxel = gameObject;
            voxelHighlightManager.build(new Matrix4f(currentVoxel.getTransform()), true);
        });

    }

    @Override
    public String getLabel() {
        return "Highlighter";
    }
}
