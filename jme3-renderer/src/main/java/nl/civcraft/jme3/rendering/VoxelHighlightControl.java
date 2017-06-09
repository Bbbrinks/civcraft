package nl.civcraft.jme3.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class VoxelHighlightControl extends AbstractControl {
    private final Node selectionBoxes;
    private final Spatial hoverSpatial;
    private final Map<GameObject, Spatial> spatials;

    @Autowired
    public VoxelHighlightControl(Node selectionBoxes,
                                 Spatial hoverSpatial,
                                 @Qualifier("voxelHighlight") PrefabManager voxelHighlightManager) {
        this.selectionBoxes = selectionBoxes;
        this.hoverSpatial = hoverSpatial;
        spatials = new HashMap<>();
        voxelHighlightManager.getGameObjectDestroyed().subscribe(this::clearHighlight);
        voxelHighlightManager.getGameObjectCreated().subscribe(this::handleAddVoxelHighlight);
    }

    public void clearHighlight(GameObject removedHighlight) {
        spatials.remove(removedHighlight);
    }

    public void handleAddVoxelHighlight(GameObject voxel) {
        Spatial clone = hoverSpatial.clone();
        clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getTransform().getTranslation().getX(), clone.getLocalTranslation().y + voxel.getTransform().getTranslation().getY(), clone.getLocalTranslation().z + voxel.getTransform().getTranslation().getZ());
        spatials.put(voxel, clone);
    }


    @Override
    protected void controlUpdate(float tpf) {
        selectionBoxes.detachAllChildren();
        for (Spatial spatial : spatials.values()) {
            selectionBoxes.attachChild(spatial);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
