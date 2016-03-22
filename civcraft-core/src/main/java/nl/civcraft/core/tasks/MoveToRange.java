package nl.civcraft.core.tasks;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.pathfinding.MoveInRangeOfVoxelTarget;

import java.util.Queue;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToRange extends Task {
    private final AStarPathFinder pathFinder;
    private final float range;
    protected Voxel target;
    private Queue<Voxel> path;
    private Civvy civvy;

    public MoveToRange(Voxel target, float range, AStarPathFinder pathFinder) {
        super(Task.State.TODO);
        this.target = target;
        this.pathFinder = pathFinder;
        this.range = range;
    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        if (this.civvy != civvy) {
            path = null;
        }
        this.civvy = civvy;
        if (path == null) {
            path = pathFinder.findPath(civvy, civvy.getCurrentVoxel(), new MoveInRangeOfVoxelTarget(range, target));
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
