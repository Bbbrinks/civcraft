package nl.civcraft.core.tasks;

import nl.civcraft.core.npc.Civvy;

/**
 * Created by Bob on 31-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public abstract class Task {

    private State state;

    public Task(State state) {
        this.state = state;
    }

    public abstract boolean affect(Civvy civvy, float tpf);

    public void completed(Civvy civvy) {
        if (!State.CONTINUAL.equals(state)) {
            setState(State.DONE);
        }
        civvy.setTask(null);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public enum State {
        TODO, DOING, DONE, CONTINUAL
    }
}
