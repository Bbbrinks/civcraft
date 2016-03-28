package nl.civcraft.core.rendering;

import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.model.events.CivvyRemoved;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.npc.Npc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class CivvyRenderer extends AbstractAppState {

    private final Node civvyNode;
    private List<Civvy> civvies;

    @Autowired
    public CivvyRenderer(Node rootNode) {
        civvyNode = new Node("civvies");
        rootNode.attachChild(civvyNode);
        civvies = new CopyOnWriteArrayList<>();
    }

    @Override
    public void update(float tpf) {
        civvyNode.detachAllChildren();
        for (Civvy civvy : civvies) {
            Npc npc = civvy.cloneNpc();
            npc.setLocalTranslation(npc.getLocalTranslation().add(civvy.getX(), civvy.getY(), civvy.getZ()));
            civvyNode.attachChild(npc);
        }
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        civvies.add(civvyCreated.getCivvy());
    }

    @EventListener
    public void removeCivvy(CivvyRemoved civvyRemoved) {
        civvies.remove(civvyRemoved.getCivvy());
    }

}
