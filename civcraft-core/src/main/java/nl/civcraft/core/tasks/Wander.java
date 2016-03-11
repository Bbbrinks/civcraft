package nl.civcraft.core.tasks;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.utils.RandomUtil;

import java.util.List;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Wander extends Task {
    private final AStarPathFinder pathFinder;

    public Wander(AStarPathFinder pathFinder) {
        super(State.CONTINUAL);
        this.pathFinder = pathFinder;
    }

    @Override
    public boolean affect(Civvy civvy, float tpf) {

        Voxel voxel = civvy.currentVoxel();
        if (voxel != null) {
            List<Voxel> possibleNextVoxels = voxel.getEnterableNeighbours();
            if (!possibleNextVoxels.isEmpty()) {
                Voxel target = possibleNextVoxels.get(RandomUtil.getNextInt(possibleNextVoxels.size()));
                civvy.setTask(new MoveTo(target, pathFinder));
            }
        }
        return false;
    }
}
