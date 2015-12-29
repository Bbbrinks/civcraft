package nl.civcraft.core.npc;

import nl.civcraft.core.model.World;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civvy {
    private final float x;
    private final float y;
    private final float z;
    private final String type;
    private final Npc npc;
    private World world;

    public Civvy(float x, float y, float z, String type, Npc npc) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.npc = npc;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Npc cloneNpc() {
        return (Npc) npc.clone();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }
}
