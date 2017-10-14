package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.MoveToVoxelTarget;
import nl.civcraft.core.pathfinding.PathFindingTarget;
import nl.civcraft.core.pathfinding.exceptions.UnreachableVoxelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveTo extends Task {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final PathFindingTarget pathFindingTarget;

    public MoveTo(GameObject target) {
        this(new MoveToVoxelTarget(target));
    }

    public MoveTo(PathFindingTarget target) {
        super(State.TODO);
        this.pathFindingTarget = target;
    }

    @Override
    public Result affect(GameObject subject,
                         float tpf) {
        GroundMovement component = subject.getComponent(GroundMovement.class).orElseThrow(() -> new IllegalStateException("Move to can only be done by GroundMovement game objects"));
        try {
            if (component.moveToward(pathFindingTarget)) {
                return Result.COMPLETED;
            }
            return Result.IN_PROGRESS;
        } catch (UnreachableVoxelException e) {
            LOGGER.debug("Could not reach target {}", pathFindingTarget, e);
            return Result.FAILED;
        }

    }


    public GameObject getTarget() {
        return pathFindingTarget.getTarget();
    }
}
