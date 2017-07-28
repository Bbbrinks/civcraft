package nl.civcraft.core.interaction.tools.walltool;

import com.jme3.math.Transform;
import nl.civcraft.core.interaction.tools.BuildWallTool;
import org.statefulj.persistence.annotations.State;

/**
 * Created by Bob on 28-7-2017.
 * <p>
 * This is probably not worth documenting
 */
public class WallToolState {

    private final BuildWallTool buildWallTool;
    @State
    String state;   // Memory Persister requires a String
    private Transform start;
    private Transform end;

    public WallToolState(BuildWallTool buildWallTool) {
        this.buildWallTool = buildWallTool;
    }


    public String getState() {
        return state;
    }

    public Transform getStart() {
        return start;
    }

    public void setStart(Transform start) {
        this.start = start;
    }

    public void reset() {
        start = null;
        end = null;
    }

    public Transform getEnd() {
        return end;
    }

    public void setEnd(Transform end) {
        this.end = end;
    }

    public void addVertical(float arg) {
        this.end.getTranslation().y += arg;
    }


    public BuildWallTool getBuildWallTool() {
        return buildWallTool;
    }
}
