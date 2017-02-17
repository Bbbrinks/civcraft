package nl.civcraft.core.pathfinding;

import com.jme3.math.Vector3f;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveInRangeOfVoxelTarget implements PathFindingTarget {


    private final float range;
    private final GameObject target;

    public MoveInRangeOfVoxelTarget(float range, GameObject target) {
        this.range = range;
        this.target = target;
    }

    @Override
    public boolean isReached(GameObject civvy, AStarNode current) {
        //TODO: Make this available for non civvy gameobjects
        Vector3f locationAtVoxel = civvy.getComponent(Civvy.class).
                map(c -> c.getLocationAt(current.getGameObject())).
                orElseThrow(() -> new IllegalStateException("Only civvies can MoveInRageOfTarget"));
        float distance = locationAtVoxel.distance(target.getTransform().getTranslation());
        return distance < range;
    }

    //TODO: move this elsewhere
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
