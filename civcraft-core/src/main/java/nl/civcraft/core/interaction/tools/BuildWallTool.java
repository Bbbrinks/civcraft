package nl.civcraft.core.interaction.tools;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.PlanningGhost;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.tools.walltool.WallToolState;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.tasks.PlaceBlock;
import org.statefulj.fsm.FSM;
import org.statefulj.fsm.TooBusyException;
import org.statefulj.fsm.model.State;
import org.statefulj.fsm.model.impl.StateImpl;
import org.statefulj.persistence.memory.MemoryPersisterImpl;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BuildWallTool implements MouseTool {

    private static final String EVENT_MOUSE_MOVEMENT = "mouseMovement";
    private static final String EVENT_LEFT_CLICK = "leftClick";
    private static final String EVENT_RIGHT_CLICK = "rightClick";


    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    private final TaskManager taskManager;
    private final PrefabManager stockpileManager;
    private final PrefabManager blockManager;
    private final PrefabManager planningGhostManager;
    private final FSM<WallToolState> wallToolStateFSM;
    private GameObject planningGhostObject;
    private final WallToolState wallToolState;

    @Inject
    public BuildWallTool(CurrentVoxelHighlighter currentVoxelHighlighter,
                         TaskManager taskManager,
                         @Named("stockpile") PrefabManager stockpileManager,
                         @Named("block") PrefabManager blockManager,
                         @Named("planningGhost") PrefabManager planningGhostManager) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
        this.taskManager = taskManager;
        this.stockpileManager = stockpileManager;
        this.blockManager = blockManager;
        this.planningGhostManager = planningGhostManager;
        this.wallToolStateFSM = buildStateMachine();
        wallToolState = new WallToolState(this);
    }

    private FSM<WallToolState> buildStateMachine() {
        State<WallToolState> initial = new StateImpl<>("initial");
        State<WallToolState> startLockedIn = new StateImpl<>("startLockedIn");
        State<WallToolState> horizontalBoundaryLockedIn = new StateImpl<>("horizontalBoundaryLockedIn");

        initial.addTransition(EVENT_MOUSE_MOVEMENT, initial, (wallToolState, event, args) -> {
            wallToolState.setStart((Transform) args[0]);
            wallToolState.getBuildWallTool().planningGhost();
        });
        initial.addTransition(EVENT_LEFT_CLICK, startLockedIn, (wallToolState, event, args) -> wallToolState.setStart((Transform) args[0]));
        initial.addTransition(EVENT_RIGHT_CLICK, initial, (wallToolState, event, args) -> wallToolState.reset());

        startLockedIn.addTransition(EVENT_MOUSE_MOVEMENT, startLockedIn, (wallToolState, event, args) -> {
            Transform end = (Transform) args[0];
            end.getTranslation().y = wallToolState.getStart().getTranslation().getY();
            wallToolState.setEnd(end);
            wallToolState.getBuildWallTool().planningGhost();
        });
        startLockedIn.addTransition(EVENT_LEFT_CLICK, horizontalBoundaryLockedIn, (wallToolState, event, args) -> wallToolState.setEnd((Transform) args[0]));
        startLockedIn.addTransition(EVENT_RIGHT_CLICK, initial, (wallToolState, event, args) -> {
            wallToolState.getBuildWallTool().clearPlanningGhost();
            wallToolState.reset();
        });

        horizontalBoundaryLockedIn.addTransition(EVENT_MOUSE_MOVEMENT, horizontalBoundaryLockedIn, (wallToolState, event, args) -> {
            wallToolState.addVertical((float) args[1]);
            wallToolState.getBuildWallTool().planningGhost();
        });
        horizontalBoundaryLockedIn.addTransition(EVENT_LEFT_CLICK, initial, (wallToolState, event, args) -> {
            wallToolState.getBuildWallTool().clearPlanningGhost();
            wallToolState.getBuildWallTool().confirmWall();
            wallToolState.reset();

        });
        horizontalBoundaryLockedIn.addTransition(EVENT_RIGHT_CLICK, initial, (wallToolState, event, args) -> {
            wallToolState.getBuildWallTool().clearPlanningGhost();
            wallToolState.reset();
        });

        List<State<WallToolState>> states = new LinkedList<>();
        states.add(initial);
        states.add(startLockedIn);
        states.add(horizontalBoundaryLockedIn);

        MemoryPersisterImpl<WallToolState> persister =
                new MemoryPersisterImpl<>(
                        states,   // Set of States
                        initial);  // Start State
        return new FSM<>("Walltool FSM", persister);
    }

    private void planningGhost() {
        if (planningGhostObject == null) {
            planningGhostObject = planningGhostManager.build(Transform.IDENTITY, true);
        }
        List<Transform> selection = new ArrayList<>();
        loopThroughSelection(selection::add);
        planningGhostObject.getComponent(PlanningGhost.class).orElseThrow(() -> new IllegalStateException("No planning ghost")).setPlannedVoxels(selection);
    }

    @Override
    public String getLabel() {
        return "Build wall";
    }

    private void clearPlanningGhost() {
        if (planningGhostObject != null) {
            planningGhostObject.destroy();
        }
        planningGhostObject = null;
    }

    private void confirmWall() {
        loopThroughSelection(transform -> taskManager.addTask(new PlaceBlock(stockpileManager, blockManager, "grassItem", transform)));
    }

    private void loopThroughSelection(Consumer<Transform> consumer) {
        Transform start = wallToolState.getStart();
        Transform end = wallToolState.getEnd();

        if (start == null) {
            return;
        }
        if (end == null) {
            consumer.accept(start);
            return;
        }
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

    @Override
    public void handleLeftClick(boolean isPressed) {
        if (!isPressed) {
            return;
        }
        try {
            Transform clone = currentVoxelHighlighter.getCurrentVoxel().getTransform().clone();
            clone.getTranslation().y += 1;
            wallToolStateFSM.onEvent(wallToolState, EVENT_LEFT_CLICK, clone);
        } catch (TooBusyException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {

        try {
            Transform clone = currentVoxelHighlighter.getCurrentVoxel().getTransform().clone();
            clone.getTranslation().y += 1;
            wallToolStateFSM.onEvent(wallToolState, EVENT_MOUSE_MOVEMENT, clone, yDiff * 10.0f);
        } catch (TooBusyException e) {
            throw new IllegalStateException(e);
        }


    }
}
