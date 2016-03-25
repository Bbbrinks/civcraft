package nl.civcraft.core.managers;


import nl.civcraft.core.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class WorldManager {

    private final World world;

    @Autowired
    public WorldManager(ApplicationEventPublisher publisher) {
        world = new World(publisher);
    }

    public World getWorld() {
        return world;
    }


}
