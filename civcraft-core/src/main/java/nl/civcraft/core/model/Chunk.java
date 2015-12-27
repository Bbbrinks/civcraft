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

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<Chunk> neighbours;
    private final int chunkX;
    private final int chunkZ;
    private final Voxel[] voxels;
    private boolean optimized;
    private List<Spatial> optimizedVoxels;
    private boolean optimizing;
    private boolean optimizingDone;

    public Chunk(int chunkX, int chunkZ, ChunkRendererControl lodControl) {
        addControl(lodControl);
        voxels = new Voxel[World.CHUNK_SIZE * 100 * World.CHUNK_SIZE];
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
        int arrayIndex = x - (chunkX * World.CHUNK_SIZE) + (y * 100) + (z * World.CHUNK_SIZE) - (chunkZ * World.CHUNK_SIZE);
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
        optimizedVoxels.forEach(this::attachChild);
        setOptimized(true);
        LOGGER.trace("End updating voxel cache");
    }

    public boolean isOptimizing() {
        return optimizing;
    }

    public void setOptimizing(boolean optimizing) {
        this.optimizing = optimizing;
    }

    public boolean isOptimizingDone() {
        return optimizingDone;
    }

    public void setOptimizingDone(boolean optimizingDone) {
        this.optimizingDone = optimizingDone;
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
        int chunkX;
        int chunkZ;
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
            default:
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

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public List<Voxel> getFaceVoxels(Face face) {
        int voxelXMin;
        int voxelXMax;
        int voxelZMin;
        int voxelZMax;
        switch (face) {
            case LEFT:
                voxelXMin = voxelXMax = this.chunkX * World.CHUNK_SIZE;
                voxelZMin = this.chunkZ * World.CHUNK_SIZE;
                voxelZMax = this.chunkZ * World.CHUNK_SIZE + World.CHUNK_SIZE;
                break;
            case RIGHT:
                voxelXMin = voxelXMax = this.chunkX * World.CHUNK_SIZE + World.CHUNK_SIZE - 1;
                voxelZMin = this.chunkZ * World.CHUNK_SIZE;
                voxelZMax = this.chunkZ * World.CHUNK_SIZE + World.CHUNK_SIZE;
                break;
            case FRONT:
                voxelZMin = voxelZMax = this.chunkZ * World.CHUNK_SIZE;
                voxelXMin = this.chunkX * World.CHUNK_SIZE;
                voxelXMax = this.chunkX * World.CHUNK_SIZE + World.CHUNK_SIZE;
                break;
            case BACK:
                voxelZMin = voxelZMax = this.chunkZ * World.CHUNK_SIZE + World.CHUNK_SIZE - 1;
                voxelXMin = this.chunkX * World.CHUNK_SIZE;
                voxelXMax = this.chunkX * World.CHUNK_SIZE + World.CHUNK_SIZE;
                break;
            case TOP:
            case BOTTOM:
            default:
                return new ArrayList<>();
        }

        final int voxelXMinF = voxelXMin;
        final int voxelXMaxF = voxelXMax;
        final int voxelZMinF = voxelZMin;
        final int voxelZMaxF = voxelZMax;

        return Arrays.asList(voxels).stream().filter(v -> v != null).filter(v -> v.getX() >= voxelXMinF && v.getX() <= voxelXMaxF && v.getZ() >= voxelZMinF && v.getZ() <= voxelZMaxF).collect(Collectors.toList());
    }
}
