package nl.civcraft.core.model;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.worldgeneration.ChunkLodOptimizerControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private List<Spatial> optimizedVoxels;
    private boolean optimizing;
    private boolean optimizingDone;

    private static final Logger LOGGER = LogManager.getLogger();

    public Chunk(int chunkSize, int chunkX, int chunkZ, ChunkLodOptimizerControl lodControl) {
        addControl(lodControl);
        voxels = new Voxel[chunkSize * 100 * chunkSize];
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
        int arrayIndex = x - (chunkX * chunkSize) + (y * 100) + (z * chunkSize) - (chunkZ * chunkSize);
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

    public void setOptimizedVoxels(List<Spatial> optimizedVoxels) {
        this.optimizedVoxels = optimizedVoxels;
    }

    public void updateVoxelCache() {
        LOGGER.trace("Start updating voxel cache");
        detachAllChildren();
        for (Spatial optimizedVoxel : optimizedVoxels) {
            attachChild(optimizedVoxel);
        }
        setOptimized(true);
        LOGGER.trace("End updating voxel cache");
    }



    public void setOptimizing(boolean optimizing) {
        this.optimizing = optimizing;
    }

    public boolean isOptimizing() {
        return optimizing;
    }

    public boolean isOptimizingDone() {
        return optimizingDone;
    }

    public void setOptimizingDone(boolean optimizingDone) {
        this.optimizingDone = optimizingDone;
    }
}
