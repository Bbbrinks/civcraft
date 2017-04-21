package nl.civcraft.core.interaction.tools;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.SelectionEvent;
import nl.civcraft.core.tasks.PlaceBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class BuildWallTool implements MouseTool {

    private static final Logger LOGGER = Logger.getLogger(BuildWallTool.class.getName());
    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    private final TaskManager taskManager;
    private final PrefabManager stockpileManager;
    private final PrefabManager blockManager;
    private final ApplicationEventPublisher eventPublisher;
    private Transform start;
    private Transform end;
    private boolean horizontalBoundaryLockedIn = false;
    private boolean startLockedIn = false;

    @Autowired
    public BuildWallTool(CurrentVoxelHighlighter currentVoxelHighlighter,
                         TaskManager taskManager,
                         @Qualifier("stockpile") PrefabManager stockpileManager,
                         @Qualifier("block") PrefabManager blockManager,
                         ApplicationEventPublisher eventPublisher) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
        this.taskManager = taskManager;
        this.stockpileManager = stockpileManager;
        this.blockManager = blockManager;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (!isPressed) {
            return;
        }
        if (!startLockedIn && start != null) {
            startLockedIn = true;
        } else if (!horizontalBoundaryLockedIn) {
            horizontalBoundaryLockedIn = true;
        } else {
            loopThroughSelection(transform -> taskManager.addTask(new PlaceBlock(stockpileManager, blockManager, "grassItem", transform)));
            start = null;
            startLockedIn = false;
            horizontalBoundaryLockedIn = false;
            end = null;
        }
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {
        GameObject currentVoxel = this.currentVoxelHighlighter.getCurrentVoxel();
        if (!startLockedIn && currentVoxel != null) {
            Transform clone = currentVoxel.getTransform().clone();
            clone.setTranslation(clone.getTranslation().add(Vector3f.UNIT_Y));
            start = clone;
        } else if (startLockedIn && !horizontalBoundaryLockedIn && currentVoxel != null) {
            end = currentVoxel.getTransform().clone();
            end.setTranslation(end.getTranslation().add(0, 1, 0));
            Vector3f subtract = end.getTranslation().subtract(start.getTranslation());
            if (Math.abs(subtract.getX()) > Math.abs(subtract.getZ())) {
                end.getTranslation().setZ(start.getTranslation().getZ());
            } else {
                end.getTranslation().setX(start.getTranslation().getX());
            }
            end.getTranslation().setY(start.getTranslation().getY());
        } else if (start != null && horizontalBoundaryLockedIn) {
            end.setTranslation(end.getTranslation().add(0, yDiff * 10f, 0));
        }
        if (start != null && end == null) {
            eventPublisher.publishEvent(new SelectionEvent(Collections.singletonList(start), this));
        } else if (start != null) {
            List<Transform> selection = new ArrayList<>();
            loopThroughSelection(transform -> selection.add(transform));
            eventPublisher.publishEvent(new SelectionEvent(selection, this));
        }

    }

    @Override
    public String getLabel() {
        return "Build wall";
    }

    private void loopThroughSelection(Consumer<Transform> consumer) {
        Vector3f origin;
        Vector3f target;
        if (start.getTranslation().length() < end.getTranslation().length()) {
            origin = start.getTranslation();
            target = end.getTranslation();
        } else {
            origin = end.getTranslation();
            target = start.getTranslation();
        }

        Vector3f toPlace = target.subtract(origin).add(1, 1, 1);
        for (float z = 0; z < toPlace.getZ(); z++) {
            for (float x = 0; x < toPlace.getX(); x++) {
                for (float y = 0; y < toPlace.getY(); y++) {
                    consumer.accept(new Transform(new Vector3f(origin.getX() + x, origin.getY() + y, origin.getZ() + z)));

                }
            }
        }
    }
}
