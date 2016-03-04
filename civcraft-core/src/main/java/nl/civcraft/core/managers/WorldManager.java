package nl.civcraft.core.managers;


import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

public class WorldManager {

    private final World world;

    @Autowired
    public WorldManager(ApplicationEventPublisher publisher) {
        world = new World(publisher);
    }

    @EventListener
    public void addCivvy(CivvyCreated civvyCreated) {
        world.addCivvy(civvyCreated.getCivvy());
    }

    public World getWorld() {
        return world;
    }


}
