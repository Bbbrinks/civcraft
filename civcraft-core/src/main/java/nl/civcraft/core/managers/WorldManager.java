package nl.civcraft.core.managers;


import nl.civcraft.core.model.World;

public class WorldManager {


    private final World world;

    public WorldManager()
    {
        world = new World();
    }

    public World getWorld() {
        return world;
    }


}
