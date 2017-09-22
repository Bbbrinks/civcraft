package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.Breakable;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;

import java.util.Optional;

import static nl.civcraft.core.tasks.Task.Result.*;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BreakBlockTask extends MoveToRange {

    private final GameObject target;

    public BreakBlockTask(GameObject target) {
        super(target, 2.0f);
        this.target = target;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        if (target.getComponent(Neighbour.class).map(n -> n.getNeighbours(NeighbourDirection.BACK, NeighbourDirection.FRONT, NeighbourDirection.LEFT, NeighbourDirection.RIGHT, NeighbourDirection
                .TOP, NeighbourDirection.BOTTOM).size() == 6).orElse(false)) {
            return FAILED;
        }

        Result inRange = super.affect(civvy, tpf);
        if (inRange.equals(COMPLETED)) {
            Optional<Breakable> component = this.target.getComponent(Breakable.class);
            if (!component.isPresent()) {
                return FAILED;
            }
            Breakable breakable = component.get();
            boolean broken = breakable.damageMe(target);
            if (broken) {
                return COMPLETED;
            } else {
                return IN_PROGRESS;
            }
        }
        return inRange;
    }

}
