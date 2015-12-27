package nl.civcraft.core.model;


import nl.civcraft.core.worldgeneration.ChunkRendererControl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    private static final int CHUNK_SIZE = 10;
    private List<Chunk> chunks;

    public World() {
        chunks = new ArrayList<>();
    }

    public void addChunk(Chunk chunk) {
        chunks.add(chunk);
    }

    public void clearChunks() {
        this.chunks.clear();
    }

    public Voxel getVoxelAt(float x, float y, float z) {
        Chunk chunkAt = getChunkAt((int) x, (int) y, (int) z);
        if (chunkAt == null) {
            return null;
        }
        return chunkAt.getVoxelAt((int) x, (int) y, (int) z);
    }

    private Chunk getChunkAt(int x, int y, int z) {
        List<Chunk> found = chunks.stream().filter(c -> c.getChunkZ() == z / CHUNK_SIZE && c.getChunkX() == x / CHUNK_SIZE).limit(1).collect(Collectors.toList());
        if (!found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    public void addVoxel(Voxel voxel, ChunkRendererControl chunkRendererControl){
        int x = voxel.getX();
        int y = voxel.getY();
        int z = voxel.getZ();
        Chunk chunkAt = getChunkAt(x, y, z);
        if(chunkAt == null){
            chunkAt = addChunkAt(x, y, z, chunkRendererControl);
        }

        voxel.addNeighbours(getVoxelNeighbours(voxel));

        chunkAt.addVoxel(voxel);
    }

    private Chunk addChunkAt(int x, int y, int z, ChunkRendererControl chunkRendererControl) {
        Chunk chunk = new Chunk(CHUNK_SIZE,x / CHUNK_SIZE, z / CHUNK_SIZE, chunkRendererControl);
        chunks.add(chunk);
        List<Chunk> neighbours = new ArrayList<>();
        addIfNotNull(neighbours, getChunkAt(x - CHUNK_SIZE, 1, z));
        addIfNotNull(neighbours, getChunkAt(x + CHUNK_SIZE, 1, z));
        addIfNotNull(neighbours, getChunkAt(x, 1, z + CHUNK_SIZE));
        addIfNotNull(neighbours, getChunkAt(x, 1, z - CHUNK_SIZE));
        chunk.addNeighbours(neighbours);
        return chunk;
    }

    public List<Chunk> getChunks()
    {
        return chunks;
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
            case TOP:
                return getVoxelAt(voxel.getX(), voxel.getY() + 1, voxel.getZ());
            case BOTTOM:
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

    private <T> void addIfNotNull(List<T> neighbours, T voxelAt) {
        if (voxelAt != null) {
            neighbours.add(voxelAt);
        }
    }

}
