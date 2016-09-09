package nl.civcraft.core.interaction.selectors;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;

import java.util.Optional;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class GroundRectangleSelector implements MouseTool {
    private static final int MAX_HEIGHT_DIFFERENCE = 10;
    protected final WorldManager worldManager;
    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    private final Node selectionBoxes;
    private final Spatial hoverSpatial;
    private Voxel currentVoxel;
    private Voxel startingVoxel;

    public GroundRectangleSelector(CurrentVoxelHighlighter currentVoxelHighlighter, Node selectionBoxes, Spatial hoverSpatial, WorldManager worldManager) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
        this.selectionBoxes = selectionBoxes;
        this.hoverSpatial = hoverSpatial;
        this.worldManager = worldManager;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (isPressed) {
            if (startingVoxel == null) {
                startingVoxel = currentVoxelHighlighter.getCurrentVoxel();
            } else {
                startSelection();
                loopThroughSelection((voxel) -> handleSelection(voxel));
                endSelection();
                selectionBoxes.detachAllChildren();
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
        int startX = startingVoxel.getX();
        int startZ = startingVoxel.getZ();
        int endX = currentVoxel.getX();
        int endZ = currentVoxel.getZ();

        if (startX > endX) {
            int tmp = startX;
            startX = endX;
            endX = tmp;
        }

        if (startZ > endZ) {
            int tmp = startZ;
            startZ = endZ;
            endZ = tmp;
        }
        for (int x = startX; x <= endX; x++) {
            for (int z = startZ; z <= endZ; z++) {
                Optional<Voxel> voxelOptional = worldManager.getWorld().getGroundAt(x, startingVoxel.getY(), z, MAX_HEIGHT_DIFFERENCE);
                if (voxelOptional.isPresent()) {
                    selectionLooper.handleElement(voxelOptional.get());
                }
            }
        }
    }

    protected abstract void handleSelection(Voxel voxel);

    protected abstract void endSelection();

    @Override
    public void handleMouseMotion() {
        if (startingVoxel == null) {
            currentVoxel = currentVoxelHighlighter.highLight();
        } else {
            currentVoxelHighlighter.clear();
            currentVoxel = currentVoxelHighlighter.getCurrentVoxel();
            selectionBoxes.detachAllChildren();
            loopThroughSelection(this::addHighlight);
        }
    }

    private void addHighlight(Voxel voxel) {
        Spatial clone = hoverSpatial.clone();
        clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getX(), clone.getLocalTranslation().y + voxel.getY(), clone.getLocalTranslation().z + voxel.getZ());
        selectionBoxes.attachChild(clone);
    }

    private void deleteBlock(int x, int z) {
        Optional<Voxel> voxelAt = worldManager.getWorld().getVoxelAt(x, startingVoxel.getY(), z);
        if (voxelAt.isPresent()) {
            voxelAt.get().breakBlock();
        }
    }

    @FunctionalInterface
    private interface SelectionLooper {
        void handleElement(Voxel voxel);
    }
}
