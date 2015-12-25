package nl.civcraft.core.model;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.worldgeneration.ChunkLodOptimizerControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk extends Node {

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

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
        this.name = chunkX + "x" + chunkZ;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public void addVoxel(Voxel voxel) {
        voxels[getArrayIndex(voxel.getX(), voxel.getY(), voxel.getZ())] = voxel;
        voxel.setChunk(this);
    }

    private int getArrayIndex(int x, int y, int z) {
        int arrayIndex = x - (chunkX * chunkSize) + (y * 100) + (z * chunkSize) - (chunkZ * chunkSize);
        if (arrayIndex < 0 || arrayIndex >= voxels.length) {
            return -1;
        }
        return arrayIndex;
    }

    public Voxel getVoxelAt(int x, int y, int z) {
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

    public int getChunkSize() {
        return chunkSize;
    }

    public void removeVoxel(Voxel voxel) {
        voxel.remove();
        int arrayIndex = getArrayIndex(voxel.getX(), voxel.getY(), voxel.getZ());
        voxels[arrayIndex] = null;
        this.optimized = false;
    }
}
