package nl.civcraft.jme3.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class CivvyControl extends AbstractControl {

    private final Node civviesNode;
    private final Node civvySpatial;
    private final List<Civvy> civvies;

    @Inject
    public CivvyControl(@Named("rootNode") Node rootNode,
                        @Named("civvy") Node civvy,
                        @Named("civvy") PrefabManager civvyManager) {
        civvyManager.getGameObjectCreated().subscribe(this::addCivvy);
        civvyManager.getGameObjectDestroyed().subscribe(this::removeCivvy);
        civviesNode = new Node("civvies");
        rootNode.attachChild(civviesNode);
        civvies = new CopyOnWriteArrayList<>();
        this.civvySpatial = civvy;
        rootNode.addControl(this);
    }

    public void addCivvy(GameObject civvyCreated) {
        Optional<Civvy> component = civvyCreated.getComponent(Civvy.class);
        if (!component.isPresent()) {
            return;
        }
        civvies.add(component.get());
    }

    public void removeCivvy(GameObject civvyRemoved) {
        Optional<Civvy> component = civvyRemoved.getComponent(Civvy.class);
        if (!component.isPresent()) {
            return;
        }
        civvies.remove(component);
    }

    @Override
    protected void controlUpdate(float tpf) {
        civviesNode.detachAllChildren();
        for (Civvy civvy : civvies) {
            Spatial npc = civvySpatial.clone();
            npc.setLocalTransform(civvy.getGameObject().getTransform());
            civviesNode.attachChild(npc);
        }
    }

    @Override
    protected void controlRender(RenderManager rm,
                                 ViewPort vp) {

    }
}
