package nl.civcraft.core.worldgeneration;

import com.jme3.scene.Node;
import nl.civcraft.core.model.Face;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk extends Node {

    private final int chunkX;
    private final int chunkZ;
    private boolean optimized;

    private final Voxel[] voxels;
    private int chunkSize;

    public Chunk(int chunkSize, int chunkX, int chunkZ, ChunkLodOptimizerControl lodControl) {
        addControl(lodControl);
        voxels = new Voxel[chunkSize * chunkSize * chunkSize];
        this.chunkSize = chunkSize;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public void addVoxel(Voxel voxel) {
        voxels[getArrayIndex(voxel.getX(), voxel.getY(), voxel.getZ())] = voxel;
    }

    private int getArrayIndex(int x, int y, int z) {
        int arrayIndex = x - (chunkX * chunkSize) + (y * chunkSize) + (z * chunkSize) - (chunkZ * chunkSize);
        if (arrayIndex < 0 || arrayIndex >= voxels.length) {
            return -1;
        }
        return arrayIndex;
    }

    public List<Voxel> getVoxelNeighbours(Voxel voxel) {
        List<Voxel> neighbours = new ArrayList<>();
        int x = voxel.getX();
        int y = voxel.getY();
        int z = voxel.getZ();
        addIfNotNull(neighbours, getVoxelAt(x - 1, y, z));
        addIfNotNull(neighbours, getVoxelAt(x, y - 1, z));
        addIfNotNull(neighbours, getVoxelAt(x, y, z - 1));
        addIfNotNull(neighbours, getVoxelAt(x + 1, y, z));
        addIfNotNull(neighbours, getVoxelAt(x, y + 1, z));
        addIfNotNull(neighbours, getVoxelAt(x, y, z + 1));
        return neighbours;
    }

    public Voxel getNeighbour(Voxel voxel, Face face) {
        switch (face) {
            case UP:
                return getVoxelAt(voxel.getX(), voxel.getY() + 1, voxel.getZ());
            case DOWN:
                return getVoxelAt(voxel.getX(), voxel.getY() - 1, voxel.getZ());
            case LEFT:
                return getVoxelAt(voxel.getX() - 1, voxel.getY(), voxel.getZ());
            case RIGHT:
                return getVoxelAt(voxel.getX() + 1, voxel.getY(), voxel.getZ());
            case FRONT:
                return getVoxelAt(voxel.getX(), voxel.getY(), voxel.getZ() - 1);
            case NONE:
                return null;
            case BACK:
                return getVoxelAt(voxel.getX(), voxel.getY(), voxel.getZ() + 1);
        }
        return null;
    }

    private void addIfNotNull(List<Voxel> neighbours, Voxel voxelAt) {
        if (voxelAt != null) {
            neighbours.add(voxelAt);
        }
    }

    private Voxel getVoxelAt(int x, int y, int z) {
        int arrayIndex = getArrayIndex(x, y, z);
        if (arrayIndex == -1) {
            return null;
        }
        return voxels[arrayIndex];
    }

    public Voxel[] getVoxels() {
        return voxels;
    }
}
