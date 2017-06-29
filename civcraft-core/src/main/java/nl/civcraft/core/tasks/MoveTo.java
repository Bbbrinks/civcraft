package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.ChangeAwarePath;
import nl.civcraft.core.pathfinding.MoveToVoxelTarget;

import java.util.Optional;
import java.util.Queue;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveTo extends Task {

    private final GameObject target;
    private ChangeAwarePath path;
    private GameObject civvy;

    public MoveTo(GameObject target) {
        super(State.TODO);
        this.target = target;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        Optional<GroundMovement> component = civvy.getComponent(GroundMovement.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Move to can only be done by GroundMovement game objects");
        }
        GroundMovement groundMovement = component.get();
        if (this.civvy != civvy) {
            path = null;
        }
        this.civvy = civvy;
        if (path == null) {
            if (groundMovement.getCurrentVoxel().equals(target)) {
                return Result.COMPLETED;
            }
            path = groundMovement.findPath(new MoveToVoxelTarget(target));
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
