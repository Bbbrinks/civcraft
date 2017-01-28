package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
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
    private Queue<GameObject> path;
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

    @Override
    public boolean canBeHandledBy(Civvy civvy) {
        return true;
    }

    public GameObject getTarget() {
        return target;
    }
}
