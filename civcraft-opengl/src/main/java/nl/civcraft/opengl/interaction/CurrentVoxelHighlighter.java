package nl.civcraft.opengl.interaction;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.opengl.raycast.MousePicker;
import nl.civcraft.opengl.rendering.Node;
import org.apache.logging.log4j.LogManager;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CurrentVoxelHighlighter implements nl.civcraft.core.interaction.util.CurrentVoxelHighlighter {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();

    private final MousePicker mousePicker;

    @Inject
    public CurrentVoxelHighlighter(MousePicker mousePicker) {
        this.mousePicker = mousePicker;
    }

    @Override
    public GameObject highLight() {
        getCurrentVoxel();
        return null;
    }

    @Override
    public Optional<GameObject> getCurrentVoxel() {
        List<Node> pick = mousePicker.pick();
        if(pick.isEmpty()){
            return Optional.empty();
        }
        Node node = pick.get(pick.size() - 1);
        LOGGER.info("Current highlight " + node + " " +node.getBoundingBox());
        return Optional.empty();
    }

    @Override
    public void clear() {

    }
}
