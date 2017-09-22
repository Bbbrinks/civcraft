package nl.civcraft.core.tasks;


import nl.civcraft.core.gamecomponents.Harvestable;
import nl.civcraft.core.model.GameObject;

import java.util.Optional;

import static nl.civcraft.core.tasks.Task.Result.COMPLETED;
import static nl.civcraft.core.tasks.Task.Result.FAILED;

public class Harvest extends MoveToRange {


    private final GameObject target;

    public Harvest(GameObject target) {
        super(target, 3.0f);
        this.target = target;
    }

    @Override
    public Result affect(GameObject civvy,
                         float tpf) {
        Result inRange = super.affect(civvy, tpf);
        if (inRange.equals(COMPLETED)) {
            Optional<Harvestable> component = this.target.getComponent(Harvestable.class);
            if (!component.isPresent()) {
                return FAILED;
            }
            Harvestable harvestable = component.get();
            Optional<GameObject> harvest = harvestable.harvest(civvy);
            return harvest.map(gameObject -> COMPLETED).orElse(FAILED);
        }
        return inRange;
    }
}
