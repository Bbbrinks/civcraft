package nl.civcraft.core.tasks.states;

import nl.civcraft.core.pathfinding.ChangeAwarePath;
import org.statefulj.persistence.annotations.State;

/**
 * Created by Bob on 4-8-2017.
 * <p>
 * This is probably not worth documenting
 */
public class MoveToState {
    @State
    String state;   // Memory Persister requires a String

    private ChangeAwarePath path;
}
