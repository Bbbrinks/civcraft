package nl.civcraft.core.interaction.selectors;

import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
@Qualifier("default")
public class SingleVoxelSelector implements MouseTool {

    private final CurrentVoxelHighlighter currentVoxelHighlighter;
    protected GameObject currentVoxel;

    @Autowired
    public SingleVoxelSelector(CurrentVoxelHighlighter currentVoxelHighlighter) {
        this.currentVoxelHighlighter = currentVoxelHighlighter;
    }

    @Override
    public void handleLeftClick(boolean isPressed) {

    }

    @Override
    public void handleMouseMotion() {
        this.currentVoxel = currentVoxelHighlighter.highLight();
    }

    @Override
    public String getLabel() {
        return "Highlighter";
    }
}
