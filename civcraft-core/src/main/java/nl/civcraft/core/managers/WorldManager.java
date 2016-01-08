package nl.civcraft.core.managers;


import com.jme3.scene.Node;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

public class WorldManager {

    private final World world;

    @Autowired
    public WorldManager(Node rootNode, TaskManager taskManager) {
        world = new World(rootNode, taskManager);
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        world.addCivvy(civvyCreated.getCivvy());
    }

    public World getWorld() {
        return world;
    }


}
