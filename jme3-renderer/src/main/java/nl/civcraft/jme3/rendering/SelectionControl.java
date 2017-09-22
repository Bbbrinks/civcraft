package nl.civcraft.jme3.rendering;

import com.jme3.math.Transform;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.gamecomponents.PlanningGhost;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class SelectionControl extends AbstractControl {
    private final Node otherSelection;
    private final Spatial hoverSpatial;
    private final List<Spatial> spatials;

    @Inject
    public SelectionControl(@Named("otherSelection") Node otherSelection,
                            @Named("rootNode") Node rootNode,
                            @Named("hoverSpatial") Spatial hoverSpatial,
                            @Named("planningGhost")
                                    PrefabManager planningGhostVoxel) {
        planningGhostVoxel.getGameObjectCreated().subscribe(this::handleNewSelection);
        planningGhostVoxel.getGameObjectChangedEvent().subscribe(this::handleNewSelection);
        planningGhostVoxel.getGameObjectDestroyed().subscribe(this::hideSelection);
        this.otherSelection = otherSelection;
        this.hoverSpatial = hoverSpatial;
        spatials = new ArrayList<>();
        rootNode.addControl(this);
    }

    private void hideSelection(GameObject gameObject) {
        spatials.clear();
    }


    public void handleNewSelection(GameObject planningGhost) {
        spatials.clear();
        PlanningGhost planningGhost1 = planningGhost.getComponent(PlanningGhost.class).orElseThrow(() -> new IllegalStateException("PlanningGhosts are not planning ghosts"));
        for (Transform transform : planningGhost1.getPlannedVoxels()) {
            Spatial clone = hoverSpatial.clone();
            clone.setLocalTransform(transform);
            spatials.add(clone);
        }

    }


    @Override
    protected void controlUpdate(float tpf) {
        otherSelection.detachAllChildren();
        for (Spatial spatial : spatials) {
            otherSelection.attachChild(spatial);
        }
    }

    @Override
    protected void controlRender(RenderManager rm,
                                 ViewPort vp) {

    }
}