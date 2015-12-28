package nl.civcraft.core.model;


import nl.civcraft.core.worldgeneration.ChunkRendererControl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    public static final int CHUNK_SIZE = 20;
    private final List<Chunk> chunks;

    public World() {
        chunks = new ArrayList<>();
    }

    public void clearChunks() {
        this.chunks.clear();
    }

    public void addVoxel(Voxel voxel, ChunkRendererControl chunkRendererControl) {
        int x = voxel.getX();
        int y = voxel.getY();
        int z = voxel.getZ();
        Chunk chunkAt = getChunkAt(x, y, z);
        if (chunkAt == null) {
            chunkAt = addChunkAt(x, y, z, chunkRendererControl);
        }

        voxel.addNeighbours(getVoxelNeighbours(voxel));

        chunkAt.addVoxel(voxel);
    }

    private Chunk getChunkAt(int x, int y, int z) {
        List<Chunk> found = chunks.stream().filter(c -> c.containsCoors(x, y, z)).limit(1).collect(Collectors.toList());
        if (!found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    private Chunk addChunkAt(int x, int y, int z, ChunkRendererControl chunkRendererControl) {
        Chunk chunk = new Chunk(x / CHUNK_SIZE, y / CHUNK_SIZE, z / CHUNK_SIZE, chunkRendererControl);
        chunks.add(chunk);
        List<Chunk> neighbours = new ArrayList<>();
        addIfNotNull(neighbours, getChunkAt(x - CHUNK_SIZE, y, z));
        addIfNotNull(neighbours, getChunkAt(x + CHUNK_SIZE, y, z));
        addIfNotNull(neighbours, getChunkAt(x, y, z + CHUNK_SIZE));
        addIfNotNull(neighbours, getChunkAt(x, y, z - CHUNK_SIZE));
        addIfNotNull(neighbours, getChunkAt(x, y + CHUNK_SIZE, z));
        addIfNotNull(neighbours, getChunkAt(x, y - CHUNK_SIZE, z));
        chunk.addNeighbours(neighbours);
        return chunk;
    }

    private List<Voxel> getVoxelNeighbours(Voxel voxel) {
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

    private <T> void addIfNotNull(List<T> neighbours, T voxelAt) {
        if (voxelAt != null) {
            neighbours.add(voxelAt);
        }
    }

    public Voxel getVoxelAt(float x, float y, float z) {
        Chunk chunkAt = getChunkAt((int) x, (int) y, (int) z);
        if (chunkAt == null) {
            return null;
        }
        return chunkAt.getVoxelAt((int) x, (int) y, (int) z);
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

}
