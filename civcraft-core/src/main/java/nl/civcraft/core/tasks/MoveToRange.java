package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.ChangeAwarePath;
import nl.civcraft.core.pathfinding.MoveInRangeOfVoxelTarget;

import java.util.Optional;
import java.util.Queue;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToRange extends Task {
    final GameObject target;
    private final float range;
    private ChangeAwarePath path;
    private GameObject currentGameObject;

    @SuppressWarnings("SameParameterValue")
    MoveToRange(GameObject target, float range) {
        super(Task.State.TODO);
        this.target = target;
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
            path = groundMovement.findPath(new MoveInRangeOfVoxelTarget(range, this.target));
            Optional<Queue<GameObject>> pathOptional = path.getCurrentPath();
            if (!pathOptional.isPresent()) {
                return Result.FAILED;
            }
        }
        Queue<GameObject> pathQueue = path.getCurrentPath().orElseThrow(() -> new IllegalStateException("fail"));
        GameObject peek = pathQueue.peek();
        if (peek == null) {
            return Result.COMPLETED;
        }
        groundMovement.moveToward(peek, tpf);
        if (groundMovement.getCurrentVoxel().equals(peek)) {
            pathQueue.poll();
        }
        return Result.IN_PROGRESS;
    }

    public GameObject getTarget() {
        return target;
    }
}
