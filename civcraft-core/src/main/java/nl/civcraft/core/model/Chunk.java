package nl.civcraft.core.model;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.rendering.ChunkRendererControl;
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
    private final int chunkY;
    private final int x;
    private final int y;
    private final int z;
    private boolean optimized;
    private List<Spatial> optimizedVoxels;
    private boolean optimizing;
    private boolean optimizingDone;

    public Chunk(int chunkX, int chunkY, int chunkZ, ChunkRendererControl lodControl) {
        addControl(lodControl);
        voxels = new Voxel[World.CHUNK_SIZE * World.CHUNK_SIZE * World.CHUNK_SIZE];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkY = chunkY;
        this.x = chunkX * World.CHUNK_SIZE;
        this.y = chunkY * World.CHUNK_SIZE;
        this.z = chunkZ * World.CHUNK_SIZE;
        this.name = chunkX + "x" + chunkY + "x" + chunkZ;
        neighbours = new ArrayList<>();
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public void addVoxel(Voxel voxel) {
        voxel.setLocalX(voxel.getX() - chunkX * World.CHUNK_SIZE);
        voxel.setLocalY(voxel.getY() - chunkY * World.CHUNK_SIZE);
        voxel.setLocalZ(voxel.getZ() - chunkZ * World.CHUNK_SIZE);
        voxels[getArrayIndex(voxel)] = voxel;
        voxel.setChunk(this);
    }

    private int getArrayIndex(Voxel voxel) {
        return getArrayIndex(voxel.getLocalX(), voxel.getLocalY(), voxel.getLocalZ());
    }

    private int getArrayIndex(int x, int y, int z) {
        int arrayIndex = (World.CHUNK_SIZE * World.CHUNK_SIZE * z) + (World.CHUNK_SIZE * y) + x;
        if (arrayIndex < 0 || arrayIndex >= voxels.length) {
            return -1;
        }
        return arrayIndex;
    }

    public Voxel getVoxelAt(int x, int y, int z) {
        int arrayIndex = getArrayIndex(x - chunkX * World.CHUNK_SIZE, y - chunkY * World.CHUNK_SIZE, z - chunkZ * World.CHUNK_SIZE);
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
        int arrayIndex = getArrayIndex(voxel.getLocalX(), voxel.getLocalY(), voxel.getLocalZ());
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
        int chunkY;
        switch (face) {
            case LEFT:
                chunkX = this.chunkX - 1;
                chunkZ = this.chunkZ;
                chunkY = this.chunkY;
                break;
            case RIGHT:
                chunkX = this.chunkX + 1;
                chunkZ = this.chunkZ;
                chunkY = this.chunkY;
                break;
            case FRONT:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ - 1;
                chunkY = this.chunkY;
                break;
            case BACK:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ + 1;
                chunkY = this.chunkY;
                break;
            case TOP:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ;
                chunkY = this.chunkY + 1;
                break;
            case BOTTOM:
                chunkX = this.chunkX;
                chunkZ = this.chunkZ;
                chunkY = this.chunkY - 1;
                break;
            default:
                return null;
        }
        final int chunkXF = chunkX;
        final int chunkZF = chunkZ;
        final int chunkYF = chunkY;
        List<Chunk> collect = neighbours.stream().filter(c -> c.getChunkZ() == chunkZF && c.getChunkX() == chunkXF && c.getChunkY() == chunkYF).limit(1).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            return collect.get(0);
        }
        return null;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public List<Voxel> getFaceVoxels(Face face) {
        int voxelXMin;
        int voxelXMax;
        int voxelZMin;
        int voxelZMax;
        int voxelYMin;
        int voxelYMax;
        //TODO refactor this so it's contained in Face
        switch (face) {
            case LEFT:
                voxelXMin = voxelXMax = x;
                voxelYMin = y;
                voxelYMax = y + World.CHUNK_SIZE;
                voxelZMin = z;
                voxelZMax = z + World.CHUNK_SIZE;
                break;
            case RIGHT:
                voxelXMin = voxelXMax = x + World.CHUNK_SIZE - 1;
                voxelYMin = y;
                voxelYMax = y + World.CHUNK_SIZE;
                voxelZMin = z;
                voxelZMax = z + World.CHUNK_SIZE;
                break;
            case FRONT:
                voxelZMin = voxelZMax = z;
                voxelYMin = y;
                voxelYMax = y + World.CHUNK_SIZE;
                voxelXMin = x;
                voxelXMax = x + World.CHUNK_SIZE;
                break;
            case BACK:
                voxelZMin = voxelZMax = z + World.CHUNK_SIZE - 1;
                voxelYMin = y;
                voxelYMax = y + World.CHUNK_SIZE;
                voxelXMin = x;
                voxelXMax = x + World.CHUNK_SIZE;
                break;
            case TOP:
                voxelYMin = voxelYMax = y + World.CHUNK_SIZE - 1;
                voxelZMin = z;
                voxelZMax = z + World.CHUNK_SIZE;
                voxelXMin = x;
                voxelXMax = x + World.CHUNK_SIZE;
                break;
            case BOTTOM:
                voxelYMin = voxelYMax = y;
                voxelZMin = z;
                voxelZMax = z + World.CHUNK_SIZE;
                voxelXMin = x;
                voxelXMax = x + World.CHUNK_SIZE;
                break;
            default:
                return new ArrayList<>();
        }

        final int voxelXMinF = voxelXMin;
        final int voxelXMaxF = voxelXMax;
        final int voxelZMinF = voxelZMin;
        final int voxelZMaxF = voxelZMax;
        final int voxelYMinF = voxelYMin;
        final int voxelYMaxF = voxelYMax;

        return Arrays.asList(voxels).stream().filter(v -> v != null).filter(v ->
                v.getX() >= voxelXMinF && v.getX() <= voxelXMaxF &&
                        v.getZ() >= voxelZMinF && v.getZ() <= voxelZMaxF &&
                        v.getY() >= voxelYMinF && v.getY() <= voxelYMaxF
        ).collect(Collectors.toList());
    }

    public boolean containsCoors(int x, int y, int z) {
        return this.x <= x && this.x + World.CHUNK_SIZE > x &&
                this.y <= y && this.y + World.CHUNK_SIZE > y &&
                this.z <= z && this.z + World.CHUNK_SIZE > z;
    }

    public int getZ() {
        return z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
