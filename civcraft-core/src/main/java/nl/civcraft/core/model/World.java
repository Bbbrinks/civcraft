package nl.civcraft.core.model;


import com.jme3.math.Vector3f;
import nl.civcraft.core.model.events.ChunkAddedEvent;
import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.model.events.CivvyRemoved;
import nl.civcraft.core.model.events.StockpileCreated;
import nl.civcraft.core.npc.Civvy;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;

public class World {

    public static final int CHUNK_SIZE = 30;
    private final List<Chunk> chunks;
    private final List<Civvy> civvies;
    private final ApplicationEventPublisher publisher;
    private final Set<Stockpile> stockpiles;

    public World(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
        chunks = new ArrayList<>();
        civvies = new ArrayList<>();
        stockpiles = new HashSet<>();
    }

    public void clearChunks() {
        this.chunks.clear();
    }

    public void addVoxel(Voxel voxel) {

        if (!getVoxelAt(voxel.getX(), voxel.getY(), voxel.getZ()).isPresent()) {

            int x = voxel.getX();
            int y = voxel.getY();
            int z = voxel.getZ();
            Optional<Chunk> chunkOptional = getChunkAt(x, y, z);
            Chunk chunkAt;
            if (chunkOptional.isPresent()) {
                chunkAt = chunkOptional.get();
            } else {
                chunkAt = addChunkAt(x, y, z);
            }
            voxel.addNeighbours(getVoxelNeighbours(voxel));

            chunkAt.addVoxel(voxel);
        }

    }

    public Optional<Voxel> getVoxelAt(float x, float y, float z) {
        return getChunkAt((int) x, (int) y, (int) z).
                map(chunk -> chunk.getVoxelAt((int) x, (int) y, (int) z)).
                orElse(Optional.empty());
    }

    private Optional<Chunk> getChunkAt(int x, int y, int z) {
        return chunks.stream().filter(c -> c.containsCoors(x, y, z)).findFirst();
    }

    private Chunk addChunkAt(int x, int y, int z) {
        double dx = x;
        double dy = y;
        double dz = z;
        Chunk chunk = new Chunk((int) Math.floor(dx / CHUNK_SIZE), (int) Math.floor(dy / CHUNK_SIZE), (int) Math.floor(dz / CHUNK_SIZE), publisher);
        chunks.add(chunk);
        List<Chunk> neighbours = new ArrayList<>();
        addIfPresent(neighbours, getChunkAt(x - CHUNK_SIZE, y, z));
        addIfPresent(neighbours, getChunkAt(x + CHUNK_SIZE, y, z));
        addIfPresent(neighbours, getChunkAt(x, y, z + CHUNK_SIZE));
        addIfPresent(neighbours, getChunkAt(x, y, z - CHUNK_SIZE));
        addIfPresent(neighbours, getChunkAt(x, y + CHUNK_SIZE, z));
        addIfPresent(neighbours, getChunkAt(x, y - CHUNK_SIZE, z));
        chunk.addNeighbours(neighbours);
        publisher.publishEvent(new ChunkAddedEvent(chunk, this));
        return chunk;
    }

    private List<Voxel> getVoxelNeighbours(Voxel voxel) {
        List<Voxel> neighbours = new ArrayList<>();
        int x = voxel.getX();
        int y = voxel.getY();
        int z = voxel.getZ();
        addIfPresent(neighbours, getVoxelAt(x - 1, y, z));
        addIfPresent(neighbours, getVoxelAt(x, y - 1, z));
        addIfPresent(neighbours, getVoxelAt(x, y, z - 1));
        addIfPresent(neighbours, getVoxelAt(x + 1, y, z));
        addIfPresent(neighbours, getVoxelAt(x, y + 1, z));
        addIfPresent(neighbours, getVoxelAt(x, y, z + 1));
        return neighbours;
    }

    private <T> void addIfPresent(List<T> neighbours, Optional<T> voxelAt) {
        if (voxelAt.isPresent()) {
            neighbours.add(voxelAt.get());
        }
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

    public Optional<Voxel> getVoxelAt(Vector3f target) {
        return getVoxelAt(target.x, target.y, target.z);
    }


    public void removeCivvy(Civvy civvy) {
        publisher.publishEvent(new CivvyRemoved(civvy, this));
        this.civvies.remove(civvy);

    }

    public void addStockpile(Stockpile createdStockpile) {
        this.stockpiles.add(createdStockpile);
        publisher.publishEvent(new StockpileCreated(createdStockpile, this));
    }

    public Optional<Voxel> getGroundAt(int x, int y, int z, int maxHeightDifference) {
        Optional<Voxel> voxelAt = getVoxelAt(x, y, z);
        if (voxelAt.isPresent()) {
            Voxel voxel = voxelAt.get();
            for (int i = 0; i < maxHeightDifference; i++) {
                Optional<Voxel> neighbour = voxel.getNeighbour(Face.TOP);
                if (!neighbour.isPresent()) {
                    return Optional.of(voxel);
                }
                voxel = neighbour.get();
            }
        } else {
            for (int i = 1; i < maxHeightDifference; i++) {
                Optional<Voxel> voxelAt1 = getVoxelAt(x, y - i, z);
                if (voxelAt1.isPresent()) {
                    return voxelAt1;
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Stockpile> getStockPile() {
        return stockpiles.stream().findAny();
    }
}
