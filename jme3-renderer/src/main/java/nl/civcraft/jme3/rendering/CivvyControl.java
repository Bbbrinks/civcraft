package nl.civcraft.jme3.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.model.events.CivvyRemoved;
import nl.civcraft.core.npc.Civvy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CivvyControl extends AbstractControl {

    private final Node civviesNode;
    private final Node civvySpatial;
    private List<Civvy> civvies;

    @Autowired
    public CivvyControl(Node rootNode, Node civvy) {
        civviesNode = new Node("civvies");
        rootNode.attachChild(civviesNode);
        civvies = new CopyOnWriteArrayList<>();
        this.civvySpatial = civvy;
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        civvies.add(civvyCreated.getCivvy());
    }

    @EventListener
    public void removeCivvy(CivvyRemoved civvyRemoved) {
        civvies.remove(civvyRemoved.getCivvy());
    }

    @Override
    protected void controlUpdate(float tpf) {
        civviesNode.detachAllChildren();
        for (Civvy civvy : civvies) {
            Spatial npc = civvySpatial.clone();
            npc.setLocalTranslation(npc.getLocalTranslation().add(civvy.getX(), civvy.getY(), civvy.getZ()));
            civviesNode.attachChild(npc);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
