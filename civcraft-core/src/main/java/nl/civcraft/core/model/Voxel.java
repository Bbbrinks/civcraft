package nl.civcraft.core.model;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel {

    private final String type;
    private final int x;
    private final int y;
    private final int z;
    private final Block block;

    public Voxel(int x, int y, int z, String type, Block block) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;

        this.block = block;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Block getBlock() {
        return block;
    }
}
