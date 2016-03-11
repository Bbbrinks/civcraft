package nl.civcraft.core.tasks;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;

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
    public boolean affect(Civvy civvy, float tpf) {
        if (this.civvy != civvy) {
            path = null;
        }
        this.civvy = civvy;
        if (path == null) {
            path = pathFinder.findPath(civvy, civvy.getCurrentVoxel(), target);
        }
        Voxel peek = path.peek();
        if (peek == null) {
            return true;
        }
        civvy.moveToward(peek, tpf);
        if (civvy.getCurrentVoxel().equals(peek)) {
            path.poll();
        }
        return false;
    }

    public Voxel getTarget() {
        return target;
    }
}
