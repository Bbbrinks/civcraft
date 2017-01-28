package nl.civcraft.core.managers;

import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class VoxelManager implements Serializable {

    private final List<Chunk> chunks;

    public VoxelManager() {
        chunks = new ArrayList<>();
    }

    public void addVoxel(GameObject voxel) {
        Optional<GameObject> voxelAt = getVoxelAt(voxel.getTransform().getTranslation());
        if (!voxelAt.isPresent()) {
            int x = Math.round(voxel.getTransform().getTranslation().getX());
            int y = Math.round(voxel.getTransform().getTranslation().getY());
            int z = Math.round(voxel.getTransform().getTranslation().getZ());
            getChunkAt(x, y, z).
                    orElseGet(() -> addChunkAt(x, y, z)).
                    addVoxel(voxel);

        } else {
            this.removeVoxel(voxelAt.get());
            addVoxel(voxel);
        }

    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    private Chunk addChunkAt(int x, int y, int z) {
        Chunk chunk = new Chunk((int) Math.floor((double) x / Chunk.CHUNK_SIZE), (int) Math.floor((double) y / Chunk.CHUNK_SIZE), (int) Math.floor((double) z / Chunk.CHUNK_SIZE));
        chunks.add(chunk);
        return chunk;
    }

    @SuppressWarnings("SameParameterValue")
    public Optional<GameObject> getGroundAt(Vector3f translation, int maxHeightDifference) {
        return getGroundAt((int) translation.getX(), (int) translation.getY(), (int) translation.getZ(), maxHeightDifference);
    }

    public Optional<GameObject> getGroundAt(float x, float y, float z, int maxHeightDifference) {
        Optional<GameObject> voxelAt = getVoxelAt(x, y, z);
        if (voxelAt.isPresent()) {
            GameObject voxel = voxelAt.get();
            for (int i = 0; i < maxHeightDifference; i++) {
                Optional<GameObject> neighbour = voxel.getComponent(Neighbour.class).map(n -> n.getNeighbour(Face.TOP)).orElse(Optional.empty());
                if (!neighbour.isPresent()) {
                    return Optional.of(voxel);
                }
                voxel = neighbour.get();
            }
        } else {
            for (int i = 1; i < maxHeightDifference; i++) {
                Optional<GameObject> voxelAt1 = getVoxelAt(x, y - i, z);
                if (voxelAt1.isPresent()) {
                    return voxelAt1;
                }
            }
        }
        return Optional.empty();
    }

    public Optional<GameObject> getVoxelAt(float x, float y, float z) {
        return getChunkAt((int) x, (int) y, (int) z).
                map(chunk -> chunk.getVoxelAt((int) x, (int) y, (int) z)).
                orElse(Optional.empty());
    }

    private Optional<Chunk> getChunkAt(int x, int y, int z) {
        return chunks.stream().filter(c -> c.containsCoors(x, y, z)).findFirst();
    }

    public void clearChunks() {
        for (Chunk chunk : chunks) {
            removeChunk(chunk);
        }
    }

    private void removeChunk(Chunk chunk) {
        for (GameObject gameObject : chunk.getVoxels()) {
            removeVoxel(gameObject);
        }
        chunks.remove(chunk);
    }

    public void removeVoxel(GameObject gameObject) {
        int x = Math.round(gameObject.getTransform().getTranslation().getX());
        int y = Math.round(gameObject.getTransform().getTranslation().getY());
        int z = Math.round(gameObject.getTransform().getTranslation().getZ());
        Optional<Chunk> chunkAt = getChunkAt(x, y, z);
        if (chunkAt.isPresent()) {
            chunkAt.get().removeVoxel(gameObject);
        } else {
            throw new IllegalStateException("Chunk not found");
        }
    }

    public Optional<Chunk> getChunkAt(GameObject voxelGameObject) {
        return getChunkAt((int) voxelGameObject.getTransform().getTranslation().getX(), (int) voxelGameObject.getTransform().getTranslation().getY(), (int) voxelGameObject.getTransform().getTranslation().getZ());
    }

    public List<GameObject> getVoxelsAt(Vector3f[] neighbourLocations) {
        List<GameObject> foundVoxels = new ArrayList<>();
        for (Vector3f neighbourLocation : neighbourLocations) {
            getVoxelAt(neighbourLocation).ifPresent(foundVoxels::add);
        }
        return foundVoxels;
    }

    public Optional<GameObject> getVoxelAt(Vector3f target) {
        return getVoxelAt(target.x, target.y, target.z);
    }
}
