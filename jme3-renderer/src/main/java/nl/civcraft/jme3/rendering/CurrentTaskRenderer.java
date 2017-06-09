package nl.civcraft.jme3.rendering;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.SystemEventPublisher;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.MoveTo;
import nl.civcraft.core.tasks.MoveToRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
class CurrentTaskRenderer {

    private final Spatial moveToSpatial;
    private final List<Civvy> civvies;
    private final Node highlightNode;

    @Autowired
    public CurrentTaskRenderer(Node rootNode,
                               Spatial moveToSpatial,
                               SystemEventPublisher systemEventPublisher,
                               @Qualifier("civvy") PrefabManager civvyManager) {
        civvyManager.getGameObjectCreated().subscribe(this::addCivvy);
        civvyManager.getGameObjectDestroyed().subscribe(this::removeCivvy);
        systemEventPublisher.getPublisher().subscribe(this::update);
        this.moveToSpatial = moveToSpatial;
        highlightNode = new Node("taskHighlights");
        rootNode.attachChild(highlightNode);
        civvies = new ArrayList<>();
    }

    @EventListener
    public void addCivvy(GameObject civvyCreated) {
        Optional<Civvy> component = civvyCreated.getComponent(Civvy.class);
        if (!component.isPresent()) {
            return;
        }
        civvies.add(component.get());
    }

    @EventListener
    public void removeCivvy(GameObject civvyRemoved) {
        Optional<Civvy> component = civvyRemoved.getComponent(Civvy.class);
        if (!component.isPresent()) {
            return;
        }
        civvies.remove(component.get());
    }

    public void update(float tpf) {
        highlightNode.detachAllChildren();
        for (Civvy civvy : civvies) {
            if (civvy.getTask() instanceof MoveTo) {
                Spatial clone = moveToSpatial.clone();
                GameObject voxel = ((MoveTo) civvy.getTask()).getTarget();
                clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getTransform().getTranslation().getX(), clone.getLocalTranslation().y + voxel.getTransform().getTranslation().getY(), clone.getLocalTranslation().z + voxel.getTransform().getTranslation().getZ());
                highlightNode.attachChild(clone);
            } else if (civvy.getTask() instanceof MoveToRange) {
                Spatial clone = moveToSpatial.clone();
                GameObject voxel = ((MoveToRange) civvy.getTask()).getTarget();
                clone.setLocalTranslation(clone.getLocalTranslation().x + voxel.getTransform().getTranslation().getX(), clone.getLocalTranslation().y + voxel.getTransform().getTranslation().getY
                        (), clone.getLocalTranslation().z + voxel.getTransform().getTranslation().getZ());
                highlightNode.attachChild(clone);
            }
        }
    }
}
