package nl.civcraft.core.model;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.worldgeneration.ChunkRendererControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk extends Node {

    private final List<Chunk> neighbours;

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

    public Chunk(int chunkSize, int chunkX, int chunkZ, ChunkRendererControl lodControl) {
        addControl(lodControl);
        voxels = new Voxel[chunkSize * 100 * chunkSize];
        this.chunkSize = chunkSize;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.name = chunkX + "x" + chunkZ;
        neighbours = new ArrayList<>();
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

    public void addNeighbours(List<Chunk> neighbours) {
        neighbours.forEach(this::addNeighbour);

    }

    private void addNeighbour(Chunk neighbour) {
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
            neighbour.addNeighbour(this);
        }

    }

    public List<Chunk> getNeighbours() {
        return neighbours;
    }

    public Chunk getNeighbour(Face face) {
        //TODO move this calculation to Face to avoid switch
        int chunkX = -1;
        int chunkZ = -1;
        switch (face) {
            case LEFT:
                chunkX = this.chunkX - 1;
                chunkZ = this.chunkZ;
                break;
            case RIGHT:
                chunkX = this.chunkX + 1;
                chunkZ = this.chunkZ;
                break;
            case FRONT:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ - 1;
                break;
            case BACK:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ + 1;
                break;
            case TOP:
            case BOTTOM:
            case NONE:
                return null;
        }
        final int chunkXF = chunkX;
        final int chunkZF = chunkZ;
        List<Chunk> collect = neighbours.stream().filter(c -> c.getChunkZ() == chunkZF && c.getChunkX() == chunkXF).limit(1).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect.get(0);
        }
        return null;
    }

    public List<Voxel> getFaceVoxels(Face face) {
        int voxelXMin = -1;
        int voxelXMax = -1;
        int voxelZMin = -1;
        int voxelZMax = -1;
        switch (face) {
            case LEFT:
                voxelXMin = voxelXMax = this.chunkX * chunkSize;
                voxelZMin = this.chunkZ * chunkSize;
                voxelZMax = this.chunkZ * chunkSize + chunkSize;
                break;
            case RIGHT:
                voxelXMin = voxelXMax = this.chunkX * chunkSize + chunkSize - 1;
                voxelZMin = this.chunkZ * chunkSize;
                voxelZMax = this.chunkZ * chunkSize + chunkSize;
                break;
            case FRONT:
                voxelZMin = voxelZMax = this.chunkZ * chunkSize;
                voxelXMin = this.chunkX * chunkSize;
                voxelXMax = this.chunkX * chunkSize + chunkSize;
                break;
            case BACK:
                voxelZMin = voxelZMax = this.chunkZ * chunkSize + chunkSize - 1;
                voxelXMin = this.chunkX * chunkSize;
                voxelXMax = this.chunkX * chunkSize + chunkSize;
                break;
            case TOP:
            case BOTTOM:
            case NONE:
                return new ArrayList<>();
        }

        final int voxelXMinF = voxelXMin;
        final int voxelXMaxF = voxelXMax;
        final int voxelZMinF = voxelZMin;
        final int voxelZMaxF = voxelZMax;

        return Arrays.asList(voxels).stream().filter(v -> v != null).filter(v -> v.getX() >= voxelXMinF && v.getX() <= voxelXMaxF && v.getZ() >= voxelZMinF && v.getZ() <= voxelZMaxF).collect(Collectors.toList());
    }
}
