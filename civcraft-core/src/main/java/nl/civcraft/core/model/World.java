package nl.civcraft.core.model;


import com.jme3.math.Vector3f;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.model.events.ChunkAddedEvent;
import nl.civcraft.core.model.events.VoxelsAddedEvent;
import nl.civcraft.core.npc.Civvy;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class World {

    public static final int CHUNK_SIZE = 60;
    private final List<Chunk> chunks;
    private final List<Civvy> civvies;
    private final ApplicationEventPublisher publisher;

    public World(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        chunks = new ArrayList<>();
        civvies = new ArrayList<>();
    }

    public void clearChunks() {
        this.chunks.clear();
    }

    public void addVoxels(List<Voxel> voxels) {
        for (Voxel voxel : voxels) {
            int x = voxel.getX();
            int y = voxel.getY();
            int z = voxel.getZ();
            Chunk chunkAt = getChunkAt(x, y, z);
            if (chunkAt == null) {
                chunkAt = addChunkAt(x, y, z);
            }

            voxel.addNeighbours(getVoxelNeighbours(voxel));

            chunkAt.addVoxel(voxel);
        }

        publisher.publishEvent(new VoxelsAddedEvent(voxels, this));
    }

    private Chunk getChunkAt(int x, int y, int z) {
        List<Chunk> found = chunks.stream().filter(c -> c.containsCoors(x, y, z)).limit(1).collect(Collectors.toList());
        if (!found.isEmpty()) {
            return found.get(0);
        }
        return null;
    }

    private Chunk addChunkAt(int x, int y, int z) {
        Chunk chunk = new Chunk(x / CHUNK_SIZE, y / CHUNK_SIZE, z / CHUNK_SIZE, publisher);
        chunks.add(chunk);
        List<Chunk> neighbours = new ArrayList<>();
        addIfNotNull(neighbours, getChunkAt(x - CHUNK_SIZE, y, z));
        addIfNotNull(neighbours, getChunkAt(x + CHUNK_SIZE, y, z));
        addIfNotNull(neighbours, getChunkAt(x, y, z + CHUNK_SIZE));
        addIfNotNull(neighbours, getChunkAt(x, y, z - CHUNK_SIZE));
        addIfNotNull(neighbours, getChunkAt(x, y + CHUNK_SIZE, z));
        addIfNotNull(neighbours, getChunkAt(x, y - CHUNK_SIZE, z));
        chunk.addNeighbours(neighbours);
        publisher.publishEvent(new ChunkAddedEvent(chunk, this));
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

    public void addCivvy(Civvy civvy) {
        civvy.setWorld(this);
        civvies.add(civvy);
        publisher.publishEvent(new CivvyCreated(civvy, this));
    }

    public List<Civvy> getCivvies() {
        return civvies;
    }

    public Voxel getVoxelAt(Vector3f target) {
        return getVoxelAt(target.x, target.y, target.z);
    }


}
