package nl.civcraft.core.model;


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

}
