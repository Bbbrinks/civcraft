package nl.civcraft.jme3.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.events.RemoveVoxelHighlights;
import nl.civcraft.core.model.events.VoxelHighlighted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class VoxelHighlightControl extends AbstractControl {
    private final Node selectionBoxes;
    private final Spatial hoverSpatial;
    private final List<Spatial> spatials;

    @Autowired
    public VoxelHighlightControl(Node selectionBoxes, Spatial hoverSpatial) {
        this.selectionBoxes = selectionBoxes;
        this.hoverSpatial = hoverSpatial;
        spatials = new ArrayList<>();
    }

    @EventListener
    public void handleClearHighlights(RemoveVoxelHighlights removeVoxelHighlights) {
        spatials.clear();
    }

    @EventListener
    public void handleAddVoxelHighlight(VoxelHighlighted voxelHighlighted) {
        Voxel voxel = voxelHighlighted.getVoxel();
        Spatial clone = hoverSpatial.clone();
        clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getX(), clone.getLocalTranslation().y + voxel.getY(), clone.getLocalTranslation().z + voxel.getZ());
        spatials.add(clone);
    }


    @Override
    protected void controlUpdate(float tpf) {
        selectionBoxes.detachAllChildren();
        for (Spatial spatial : spatials) {
            selectionBoxes.attachChild(spatial);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
