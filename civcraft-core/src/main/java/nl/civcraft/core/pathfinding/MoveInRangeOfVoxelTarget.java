package nl.civcraft.core.pathfinding;

import com.jme3.math.Vector3f;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveInRangeOfVoxelTarget implements PathFindingTarget {

    private static final Logger LOGGER = LogManager.getLogger();

    private final float range;
    private final Voxel target;

    public MoveInRangeOfVoxelTarget(float range, Voxel target) {
        this.range = range;
        this.target = target;
    }

    @Override
    public boolean isReached(Civvy civvy, AStarNode current) {
        Vector3f locationAtVoxel = civvy.getLocationAtVoxel(current.getVoxel());
        float distance = locationAtVoxel.distance(target.getLocation());
        LOGGER.warn("Distance " + distance);
        return distance < range;
    }

    @Override
    public int getCostFrom(AStarNode next) {
        int xCost = Math.abs(next.getVoxel().getX() - target.getX());
        int yCost = Math.abs(next.getVoxel().getY() - target.getY());
        int zCost = Math.abs(next.getVoxel().getZ() - target.getZ());
        return xCost + yCost + zCost;
    }

    @Override
    public int getMaxSearchArea(Voxel start) {
        return (int) (target.getLocation().distance(start.getLocation()) * 5);
    }
}
