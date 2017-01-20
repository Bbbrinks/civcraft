package nl.civcraft.core.tasks;


import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.managers.EntityManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.AStarPathFinder;

import java.util.Optional;

import static nl.civcraft.core.tasks.Task.Result.COMPLETED;
import static nl.civcraft.core.tasks.Task.Result.FAILED;

public class Harvest extends MoveToRange {

    private final EntityManager entityManager;

    public Harvest(EntityManager entityManager, GameObject target, AStarPathFinder pathFinder) {
        super(target, 3.0f, pathFinder);
        this.entityManager = entityManager;
    }

    @Override
    public Result affect(GameObject target, float tpf) {
        Result inRange = super.affect(target, tpf);
        if (inRange.equals(COMPLETED)) {
            Optional<Harvestable> component = this.target.getComponent(Harvestable.class);
            if (!component.isPresent()) {
                return FAILED;
            }
            Harvestable harvestable = component.get();
            Optional<GameObject> harvest = harvestable.harvest(target);
            if (harvest.isPresent()) {
                entityManager.addEntity(harvest.get(), target.getTransform());
                return COMPLETED;
            } else {
                return FAILED;
            }
        }
        return inRange;
    }
}
