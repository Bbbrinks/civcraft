package nl.civcraft.core.rendering;

import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.MoveTo;
import nl.civcraft.core.tasks.MoveToRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurrentTaskRenderer extends AbstractAppState {

    private final Spatial moveToSpatial;
    private List<Civvy> civvies;
    private Node highlightNode;

    @Autowired
    public CurrentTaskRenderer(Node rootNode, Spatial moveToSpatial) {
        this.moveToSpatial = moveToSpatial;
        highlightNode = new Node("taskHighlights");
        rootNode.attachChild(highlightNode);
        civvies = new ArrayList<>();
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        civvies.add(civvyCreated.getCivvy());
    }

    @Override
    public void update(float tpf) {
        highlightNode.detachAllChildren();
        for (Civvy civvy : civvies) {
            if (civvy.getTask() instanceof MoveTo) {
                Spatial clone = moveToSpatial.clone();
                Voxel voxel = ((MoveTo) civvy.getTask()).getTarget();
                clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getX(), clone.getLocalTranslation().y + voxel.getY(), clone.getLocalTranslation().z + voxel.getZ());
                highlightNode.attachChild(clone);
            } else if (civvy.getTask() instanceof MoveToRange) {
                Spatial clone = moveToSpatial.clone();
                Voxel voxel = ((MoveToRange) civvy.getTask()).getTarget();
                clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getX(), clone.getLocalTranslation().y + voxel.getY(), clone.getLocalTranslation().z + voxel.getZ());
                highlightNode.attachChild(clone);
            }
        }
    }
}
