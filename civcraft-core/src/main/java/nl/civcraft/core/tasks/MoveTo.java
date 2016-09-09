package nl.civcraft.core.tasks;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.pathfinding.MoveToVoxelTarget;

import java.util.Queue;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveTo extends Task {


    private final AStarPathFinder pathFinder;
    protected Voxel target;
    private Queue<Voxel> path;
    private Civvy civvy;

    public MoveTo(Voxel target, AStarPathFinder pathFinder) {
        super(State.TODO);
        this.target = target;
        this.pathFinder = pathFinder;
    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        if (this.civvy != civvy) {
            path = null;
        }
        this.civvy = civvy;
        if (path == null) {
            if (civvy.getCurrentVoxel().equals(target)) {
                return Result.COMPLETED;
            }
            path = pathFinder.findPath(civvy, civvy.getCurrentVoxel(), new MoveToVoxelTarget(target));
            if (path == null) {
                return Result.FAILED;
            }
        }
        Voxel peek = path.peek();
        if (peek == null) {
            return Result.COMPLETED;
        }
        civvy.moveToward(peek, tpf);
        if (civvy.getCurrentVoxel().equals(peek)) {
            path.poll();
        }
        return Result.IN_PROGRESS;
    }

    public Voxel getTarget() {
        return target;
    }
}
