package nl.civcraft.core.model;

import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

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
    private final List<Voxel> neighbours;
    private Chunk chunk;

    public Voxel(int x, int y, int z, String type, Block block) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;

        this.block = block;

        neighbours = new ArrayList<>();
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

    public Block cloneBlock() {
        return (Block) block.clone();
    }

    public void breakBlock() {
        chunk.removeVoxel(this);
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public boolean isVisible() {
        if (y == 0) {
            return neighbours.size() != 5;
        }
        return neighbours.size() != 6;
    }

    private void addNeighbour(Voxel voxel) {
        if (!neighbours.contains(voxel)) {
            neighbours.add(voxel);
            voxel.addNeighbour(this);
        }
    }

    private void removeNeighbour(Voxel voxel) {
        if (neighbours.contains(voxel)) {
            voxel.removeNeighbour(voxel);
            neighbours.remove(voxel);
        }
    }

    public void remove() {
        for (Voxel neighbour : neighbours) {
            neighbour.removeNeighbour(this);
        }
    }

    public void addNeighbours(List<Voxel> voxelNeighbours) {
        voxelNeighbours.forEach(this::addNeighbour);
    }

    public List<Voxel> getNeighbours() {
        return neighbours;
    }

    public Vector3f findLocalChunkTranslation() {
        return new Vector3f(x, y, z).subtract(new Vector3f(chunk.getChunkX() * World.CHUNK_SIZE, 0, chunk.getChunkZ() * World.CHUNK_SIZE));
    }

    @Override
    public String toString() {
        return type + "@" + x + "x" + y + "x" + z;
    }
}
