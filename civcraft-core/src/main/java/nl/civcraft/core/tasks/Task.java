package nl.civcraft.core.tasks;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;

import java.io.Serializable;

/**
 * Created by Bob on 31-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public abstract class Task implements Serializable {

    private State state;

    Task(State state) {
        this.state = state;
    }

    public abstract Result affect(GameObject target, float tpf);

    public void completed(Civvy civvy) {
        if (!State.CONTINUAL.equals(state)) {
            setState(State.DONE);
        }
        civvy.setTask(null);
    }

    public void failed(Civvy civvy) {
        if (!State.CONTINUAL.equals(state)) {
            setState(State.FAILED);
        }
        civvy.setTask(null);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }


    public enum Result {
        COMPLETED, FAILED, IN_PROGRESS
    }

    public enum State {
        TODO, DOING, DONE, FAILED, CONTINUAL
    }
}
