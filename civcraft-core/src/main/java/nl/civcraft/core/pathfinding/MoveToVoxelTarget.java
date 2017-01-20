package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToVoxelTarget implements PathFindingTarget {
    private final GameObject target;

    public MoveToVoxelTarget(GameObject target) {
        this.target = target;
    }

    @Override
    public boolean isReached(GameObject gameObject, AStarNode current) {
        return target.equals(current.getGameObject());
    }

    @Override
    public int getCostFrom(AStarNode next) {
        float xCost = Math.abs(next.getGameObject().getTransform().getTranslation().getX() - target.getTransform().getTranslation().getX());
        float yCost = Math.abs(next.getGameObject().getTransform().getTranslation().getY() - target.getTransform().getTranslation().getY());
        float zCost = Math.abs(next.getGameObject().getTransform().getTranslation().getZ() - target.getTransform().getTranslation().getZ());
        return (int) (xCost + yCost + zCost);
    }


    @Override
    public int getMaxSearchArea(GameObject start) {
        return (int) (target.getTransform().getTranslation().distance(start.getTransform().getTranslation()) * 5);
    }
}
