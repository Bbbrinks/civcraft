package nl.civcraft.core.tasks;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MoveTo extends Task {

    protected Voxel target;

    public MoveTo(State state) {
        super(state);
    }

    public MoveTo(Voxel target) {
        super(State.DOING);
        this.target = target;
    }

    @Override
    public boolean affect(Civvy civvy, float tpf) {
        if (target == null || civvy.distance(target) < 0.01f) {
            civvy.setCurrentVoxel(target);
            return true;
        }
        civvy.moveToward(target, tpf);
        return false;
    }
}
