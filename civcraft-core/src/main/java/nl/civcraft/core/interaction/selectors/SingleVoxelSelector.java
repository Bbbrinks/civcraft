package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.model.GameObject;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Named("singleVoxelSelector")
public class SingleVoxelSelector implements MouseTool {

    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    protected GameObject currentVoxel;

    @Inject
    public SingleVoxelSelector(CurrentVoxelHighlighter currentVoxelHighlighter) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {
        //noop
    }

    @Override
    public void handleMouseMotion(float xDiff,
                                  float yDiff) {
        this.currentVoxel = currentVoxelHighlighter.highLight();
    }

    @Override
    public String getLabel() {
        return "Highlighter";
    }
}
