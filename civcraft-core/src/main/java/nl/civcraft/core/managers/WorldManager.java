package nl.civcraft.core.managers;


import com.jme3.scene.Node;
import nl.civcraft.core.model.World;
import org.springframework.beans.factory.annotation.Autowired;

public class WorldManager {



    private final World world;

    @Autowired
    public WorldManager(Node rootNode)
    {
        world = new World(rootNode);
    }

    public World getWorld() {
        return world;
    }


}
