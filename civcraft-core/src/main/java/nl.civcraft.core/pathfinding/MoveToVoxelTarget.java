package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;
import org.joml.Vector3f;

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
    public boolean isReached(GameObject gameObject,
                             AStarNode current) {
        return target.equals(current.getGameObject());
    }

    @Override
    public int getCostFrom(AStarNode next) {
        Vector3f tragetTranslation = target.getTransform().getTranslation(new Vector3f());
        Vector3f nextTranslation = next.getGameObject().getTransform().getTranslation(new Vector3f());
        float xCost = Math.abs(nextTranslation.x() - tragetTranslation.x());
        float yCost = Math.abs(nextTranslation.y() - tragetTranslation.y());
        float zCost = Math.abs(nextTranslation.z() - tragetTranslation.z());
        return (int) (xCost + yCost + zCost);
    }


    @Override
    public int getMaxSearchArea(GameObject start) {
        return (int) (target.getTransform().getTranslation(new Vector3f()).distance(start.getTransform().getTranslation(new Vector3f())) * 5);
    }

    public GameObject getTarget() {
        return target;
    }
}
