package nl.civcraft.core.interaction.tools.walltool;


import nl.civcraft.core.interaction.tools.BuildWallTool;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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
    private Matrix4f start;
    private Matrix4f end;

    public WallToolState(BuildWallTool buildWallTool) {
        this.buildWallTool = buildWallTool;
    }


    public String getState() {
        return state;
    }

    public Matrix4f getStart() {
        return start;
    }

    public void setStart(Matrix4f start) {
        this.start = start;
    }

    public void reset() {
        start = null;
        end = null;
    }

    public Matrix4f getEnd() {
        return end;
    }

    public void setEnd(Matrix4f end) {
        this.end = end;
    }

    public void addVertical(float arg) {
        this.end.translate(new Vector3f(0, arg, 0));
    }


    public BuildWallTool getBuildWallTool() {
        return buildWallTool;
    }
}
