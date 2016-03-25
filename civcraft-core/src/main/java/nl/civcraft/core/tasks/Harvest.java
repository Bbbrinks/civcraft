package nl.civcraft.core.tasks;


import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;

import java.util.Optional;

import static nl.civcraft.core.tasks.Task.Result.COMPLETED;
import static nl.civcraft.core.tasks.Task.Result.FAILED;

public class Harvest extends MoveToRange {

    public Harvest(Voxel target, AStarPathFinder pathFinder) {
        super(target, 3.0f, pathFinder);
    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        Result inRange = super.affect(civvy, tpf);
        if (inRange.equals(COMPLETED)) {
            Optional<Harvestable> component = target.getComponent(Harvestable.class);
            if (!component.isPresent()) {
                return FAILED;
            }
            Harvestable harvestable = component.get();
            return harvestable.harvest(civvy);
        }
        return inRange;
    }
}
