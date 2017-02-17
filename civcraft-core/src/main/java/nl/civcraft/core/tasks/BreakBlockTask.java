package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.Breakable;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;

import java.util.Optional;

import static nl.civcraft.core.tasks.Task.Result.*;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BreakBlockTask extends MoveToRange {

    public BreakBlockTask(GameObject target) {
        super(target, 3.0f);
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
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

    @Override
    public boolean canBeHandledBy(Civvy civvy) {
        return target.getComponent(Neighbour.class).map(n -> n.getDirectNeighbours().size() < 6).orElse(false);
    }
}
