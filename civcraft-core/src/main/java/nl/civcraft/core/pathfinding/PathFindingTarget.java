package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface PathFindingTarget {
    boolean isReached(Civvy civvy, AStarNode current);

    int getCostFrom(AStarNode next);

    int getMaxSearchArea(Voxel start);
}
