package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.pathfinding.MoveInRangeOfVoxelTarget;

import java.util.Optional;
import java.util.Queue;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToRange extends Task {
    private final AStarPathFinder pathFinder;
    private final float range;
    protected GameObject target;
    private Queue<GameObject> path;
    private GameObject currentGameObject;

    public MoveToRange(GameObject target, float range, AStarPathFinder pathFinder) {
        super(Task.State.TODO);
        this.target = target;
        this.pathFinder = pathFinder;
        this.range = range;
    }

    @Override
    public Result affect(GameObject target, float tpf) {
        Optional<GroundMovement> component = target.getComponent(GroundMovement.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Move to can only be done by GroundMovement game objects");
        }
        GroundMovement groundMovement = component.get();
        if (this.currentGameObject != target) {
            path = null;
        }
        this.currentGameObject = target;
        if (path == null) {
            path = pathFinder.findPath(currentGameObject, groundMovement.getCurrentVoxel(), new MoveInRangeOfVoxelTarget(range, this.target));
            if (path == null) {
                return Result.FAILED;
            }
        }
        GameObject peek = path.peek();
        if (peek == null) {
            return Result.COMPLETED;
        }
        groundMovement.moveToward(peek, tpf);
        if (groundMovement.getCurrentVoxel().equals(peek)) {
            path.poll();
        }
        return Result.IN_PROGRESS;
    }

    public GameObject getTarget() {
        return target;
    }
}
