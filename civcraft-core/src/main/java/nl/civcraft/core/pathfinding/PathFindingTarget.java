package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
interface PathFindingTarget {
    boolean isReached(GameObject gameObject, AStarNode current);

    int getCostFrom(AStarNode next);

    int getMaxSearchArea(GameObject start);
}
