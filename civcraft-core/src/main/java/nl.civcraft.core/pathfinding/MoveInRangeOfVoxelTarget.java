package nl.civcraft.core.pathfinding;


import nl.civcraft.core.gamecomponents.Civvy;
import nl.civcraft.core.model.GameObject;
import org.joml.Vector3f;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveInRangeOfVoxelTarget implements PathFindingTarget {


    private final float range;
    private final GameObject target;

    public MoveInRangeOfVoxelTarget(float range,
                                    GameObject target) {
        this.range = range;
        this.target = target;
    }

    @Override
    public boolean isReached(GameObject civvy,
                             AStarNode current) {
        //TODO: Make this available for non civvy gameobjects
        Vector3f locationAtVoxel = civvy.getComponent(Civvy.class).
                map(c -> c.getLocationAt(current.getGameObject())).
                orElseThrow(() -> new IllegalStateException("Only civvies can MoveInRageOfTarget"));
        float distance = locationAtVoxel.distance(target.getTransform().getTranslation(new Vector3f()));
        return distance < range;
    }

    //TODO: move this elsewhere
    @Override
    public int getCostFrom(AStarNode next) {
        Vector3f targetTranslation = target.getTransform().getTranslation(new Vector3f());
        Vector3f nextNodeTranslation = next.getGameObject().getTransform().getTranslation(new Vector3f());
        float xCost = Math.abs(nextNodeTranslation.x() - targetTranslation.x());
        float yCost = Math.abs(nextNodeTranslation.y() - targetTranslation.y());
        float zCost = Math.abs(nextNodeTranslation.z() - targetTranslation.z());
        return (int) (xCost + yCost + zCost);
    }

    @Override
    public int getMaxSearchArea(GameObject start) {
        return (int) (target.getTransform().getTranslation(new Vector3f()).distance(start.getTransform().getTranslation(new Vector3f())) * 5);
    }

    @Override
    public GameObject getTarget() {
        return target;
    }
}
