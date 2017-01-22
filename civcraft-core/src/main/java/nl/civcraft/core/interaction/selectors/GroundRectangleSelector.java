package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.RemoveVoxelHighlights;
import nl.civcraft.core.model.events.VoxelHighlighted;
import org.springframework.context.ApplicationEventPublisher;

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
    private final ApplicationEventPublisher eventPublisher;
    protected GameObject startingVoxel;
    private GameObject currentVoxel;

    public GroundRectangleSelector(CurrentVoxelHighlighter currentVoxelHighlighter, ApplicationEventPublisher eventPublisher, VoxelManager voxelManager) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
        this.voxelManager = voxelManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (isPressed) {
            if (startingVoxel == null) {
                startingVoxel = currentVoxelHighlighter.getCurrentVoxel();
            } else {
                startSelection();
                loopThroughSelection(this::handleSelection);
                endSelection();
                eventPublisher.publishEvent(new RemoveVoxelHighlights(this));
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
        float startX = startingVoxel.getTransform().getTranslation().getX();
        float startZ = startingVoxel.getTransform().getTranslation().getZ();
        float endX = currentVoxel.getTransform().getTranslation().getX();
        float endZ = currentVoxel.getTransform().getTranslation().getZ();

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
                Optional<GameObject> voxelOptional = voxelManager.getGroundAt(x, startingVoxel.getTransform()
                                .getTranslation().getY(), z,
                        MAX_HEIGHT_DIFFERENCE);
                voxelOptional.ifPresent(selectionLooper::handleElement);
            }
        }
    }

    protected abstract void endSelection();

    @Override
    public void handleMouseMotion() {
        if (startingVoxel == null) {
            currentVoxel = currentVoxelHighlighter.highLight();
        } else {
            currentVoxelHighlighter.clear();
            currentVoxel = currentVoxelHighlighter.getCurrentVoxel();
            eventPublisher.publishEvent(new RemoveVoxelHighlights(this));
            loopThroughSelection(this::addHighlight);
        }
    }

    protected abstract void handleSelection(GameObject voxel);

    private void addHighlight(GameObject voxel) {
        eventPublisher.publishEvent(new VoxelHighlighted(voxel, this));
    }

    @FunctionalInterface
    private interface SelectionLooper {
        void handleElement(GameObject voxel);
    }
}
