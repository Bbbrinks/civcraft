package nl.civcraft.core.model;

import nl.civcraft.core.model.events.ChunkModifiedEvent;
import nl.civcraft.core.model.events.VoxelRemovedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk {


    private final List<Chunk> neighbours;
    private final int chunkX;
    private final int chunkZ;
    private final Voxel[] voxels;
    private final int chunkY;
    private final int x;
    private final int y;
    private final int z;
    private final String name;
    private final ApplicationEventPublisher publisher;


    public Chunk(int chunkX, int chunkY, int chunkZ, ApplicationEventPublisher publisher) {
        this.publisher = publisher;
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


    public void addVoxel(Voxel voxel) {
        voxel.setLocalX(voxel.getX() - chunkX * World.CHUNK_SIZE);
        voxel.setLocalY(voxel.getY() - chunkY * World.CHUNK_SIZE);
        voxel.setLocalZ(voxel.getZ() - chunkZ * World.CHUNK_SIZE);
        voxels[getArrayIndex(voxel)] = voxel;
        voxel.setChunk(this);
        publisher.publishEvent(new ChunkModifiedEvent(this, this));
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

    public Optional<Voxel> getVoxelAt(int x, int y, int z) {
        int arrayIndex = getArrayIndex(x - chunkX * World.CHUNK_SIZE, y - chunkY * World.CHUNK_SIZE, z - chunkZ * World.CHUNK_SIZE);
        if (arrayIndex == -1) {
            return Optional.empty();
        }
        return Optional.ofNullable(voxels[arrayIndex]);
    }

    public Voxel[] getVoxels() {
        return voxels;
    }


    public void removeVoxel(Voxel voxel) {
        voxel.remove();
        int arrayIndex = getArrayIndex(voxel.getLocalX(), voxel.getLocalY(), voxel.getLocalZ());
        voxels[arrayIndex] = null;
        publisher.publishEvent(new VoxelRemovedEvent(voxel, this));
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

    public String getName() {
        return name;
    }
}
