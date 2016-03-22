package nl.civcraft.core.tasks;


import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;

import static nl.civcraft.core.tasks.Task.Result.COMPLETED;

public class Harvest extends MoveToRange {

    public Harvest(Voxel target, AStarPathFinder pathFinder) {
        super(target, 3.0f, pathFinder);
    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        Result inRange = super.affect(civvy, tpf);
        if (inRange.equals(COMPLETED)) {
            civvy.harvest(target);
            return COMPLETED;
        }
        return inRange;
    }
}
