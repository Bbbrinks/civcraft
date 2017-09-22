package nl.civcraft.core.tasks;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.MoveInRangeOfVoxelTarget;

/**
 * Created by Bob on 22-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToRange extends MoveTo {

    @SuppressWarnings("SameParameterValue")
    MoveToRange(GameObject target, float range) {
        super(new MoveInRangeOfVoxelTarget(range, target));
    }
}
