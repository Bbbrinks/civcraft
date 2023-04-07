package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Named;
import java.util.Optional;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class GroundRectangleSelector implements MouseTool {
    private static final int MAX_HEIGHT_DIFFERENCE = 10;
    private final VoxelManager voxelManager;
    protected final MousePicker mousePicker;
    private final PrefabManager voxelHighlightManager;
    protected GameObject startingVoxel;
    private GameObject currentVoxel;


    public GroundRectangleSelector(MousePicker mousePicker,
                                   VoxelManager voxelManager,
                                   @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        this.mousePicker = mousePicker;
        this.voxelManager = voxelManager;
        this.voxelHighlightManager = voxelHighlightManager;
    }

    @Override
    public void handleLeftClick() {
        if (startingVoxel == null) {
            startingVoxel = mousePicker.pickNearest(Voxel.class).orElse(null);
        } else {
            startSelection();
            loopThroughSelection(this::handleSelection);
            endSelection();
            clearHighlights();
            startingVoxel = null;
        }
    }

    protected abstract void startSelection();

    private void loopThroughSelection(SelectionLooper selectionLooper) {
        if (startingVoxel.equals(currentVoxel)) {
            selectionLooper.handleElement(startingVoxel);
            return;
        }
        Vector3f startingVoxelTranslation = startingVoxel.getTransform().getTranslation(new Vector3f());
        float startX = startingVoxelTranslation.x();
        float startZ = startingVoxelTranslation.z();
        Vector3f currentVoxelTranslation = currentVoxel.getTransform().getTranslation(new Vector3f());
        float endX = currentVoxelTranslation.x();
        float endZ = currentVoxelTranslation.z();

        if (startX > endX) {
            float tmp = startX;
            startX = endX;
            endX = tmp;
        }

        if (startZ > endZ) {
            float tmp = startZ;
            startZ = endZ;
            endZ = tmp;
        }
        for (float x = startX; x <= endX; x++) {
            for (float z = startZ; z <= endZ; z++) {
                Optional<GameObject> voxelOptional = voxelManager.getGroundAt(x, startingVoxelTranslation.y(), z,
                        MAX_HEIGHT_DIFFERENCE);
                voxelOptional.ifPresent(selectionLooper::handleElement);
            }
        }
    }

    protected abstract void endSelection();

    private void clearHighlights() {
        voxelHighlightManager.destroyAll();
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {
        clearHighlights();
        if (startingVoxel == null) {
            Optional<GameObject> gameObject = mousePicker.pickNearest(Voxel.class);
            if(gameObject.isPresent()){
                currentVoxel = gameObject.get();
                addHighlight(currentVoxel);
            } else {
                currentVoxel = null;
            }
        } else {
            currentVoxel = mousePicker.pickNearest(Voxel.class).orElse(null);
            loopThroughSelection(this::addHighlight);
        }
    }

    protected abstract void handleSelection(GameObject voxel);

    private void addHighlight(GameObject voxel) {
       voxelHighlightManager.build(new Matrix4f(voxel.getTransform()), true);
    }

    @FunctionalInterface
    private interface SelectionLooper {
        void handleElement(GameObject voxel);
    }
}
