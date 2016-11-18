package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToVoxelTarget implements PathFindingTarget {
    private final Voxel target;

    public MoveToVoxelTarget(Voxel target) {
        this.target = target;
    }

    @Override
    public boolean isReached(GameObject gameObject, AStarNode current) {
        return target.equals(current.getVoxel());
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
