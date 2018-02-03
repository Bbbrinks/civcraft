package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class GroundRectangleSelector implements MouseTool {
    private static final int MAX_HEIGHT_DIFFERENCE = 10;
    private final VoxelManager voxelManager;
    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    private final PrefabManager voxelHighlightManager;
    private final List<GameObject> highlightedVoxels;
    protected GameObject startingVoxel;
    private GameObject currentVoxel;


    public GroundRectangleSelector(CurrentVoxelHighlighter currentVoxelHighlighter,
                                   VoxelManager voxelManager,
                                   @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
        this.voxelManager = voxelManager;
        this.voxelHighlightManager = voxelHighlightManager;
        highlightedVoxels = new ArrayList<>();
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (isPressed) {
            if (startingVoxel == null) {
                startingVoxel = currentVoxelHighlighter.getCurrentVoxel().orElse(null);
            } else {
                startSelection();
                loopThroughSelection(this::handleSelection);
                endSelection();
                clearHighlights();
                startingVoxel = null;
            }
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
        highlightedVoxels.forEach(GameObject::destroy);
        highlightedVoxels.clear();
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {
        if (startingVoxel == null) {
            currentVoxel = currentVoxelHighlighter.highLight();
        } else {
            currentVoxelHighlighter.clear();
            currentVoxel = currentVoxelHighlighter.getCurrentVoxel().orElse(null);
            clearHighlights();
            loopThroughSelection(this::addHighlight);
        }
    }

    protected abstract void handleSelection(GameObject voxel);

    private void addHighlight(GameObject voxel) {

        GameObject build = voxelHighlightManager.build(new Matrix4f(voxel.getTransform()), true);
        highlightedVoxels.add(build);
    }

    @FunctionalInterface
    private interface SelectionLooper {
        void handleElement(GameObject voxel);
    }
}
