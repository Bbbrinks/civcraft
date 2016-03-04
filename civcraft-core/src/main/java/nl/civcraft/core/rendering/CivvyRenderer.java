package nl.civcraft.core.rendering;

import com.jme3.app.state.AbstractAppState;
import com.jme3.scene.Node;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.npc.Npc;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class CivvyRenderer extends AbstractAppState {

    private final Node civvyNode;
    private List<Civvy> civvies;

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

}
