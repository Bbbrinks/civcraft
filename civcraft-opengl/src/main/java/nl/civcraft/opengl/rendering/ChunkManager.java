package nl.civcraft.opengl.rendering;

import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Bob on 2-2-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class ChunkManager {

    private final Node chunks;
    private final Map<Integer, Map<Integer, Map<Integer, Node>>> chunkMap;
    private static final int CHUNK_SIZE = 50;

    @Inject
    public ChunkManager(@Named("rootNode") Node rootNode) {
        this.chunks = new Node("chunks", rootNode);
        chunkMap = new HashMap<>();
    }

    public Node getOrCreateChunk(Vector3f includingCoordinates) {
        return getChunk(includingCoordinates).orElseGet(() -> createChunk(includingCoordinates));
    }

    private Node createChunk(Vector3f includingCoordinates) {

        int chunkMinX = (int) (includingCoordinates.x / CHUNK_SIZE);
        int chunkMinY = (int) (includingCoordinates.y / CHUNK_SIZE);
        int chunkMinZ = (int) (includingCoordinates.z / CHUNK_SIZE);
        Vector3f boundMin = new Vector3f(chunkMinX, chunkMinY, chunkMinZ);
        Node node = new Node("Chunk " + boundMin.toString(), chunks);
        addChunkToMap(chunkMinX, chunkMinY, chunkMinZ, node);
        return node;
    }

    private void addChunkToMap(int x,
                               int y,
                               int z,
                               Node node) {

        Map<Integer, Map<Integer, Node>> xMap = chunkMap.computeIfAbsent(x, k -> new HashMap<>());
        Map<Integer, Node> yMap = xMap.computeIfAbsent(y, k -> new HashMap<>());
        yMap.put(z, node);
    }

    private Optional<Node> getChunk(Vector3f includingCoordinates) {
        int chunkMinX = (int) (includingCoordinates.x / CHUNK_SIZE);
        int chunkMinY = (int) (includingCoordinates.y / CHUNK_SIZE);
        int chunkMinZ = (int) (includingCoordinates.z / CHUNK_SIZE);
        Map<Integer, Map<Integer, Node>> xMap = chunkMap.get(chunkMinX);
        if (xMap == null) {
            return Optional.empty();
        }
        Map<Integer, Node> yMap = xMap.get(chunkMinY);
        if (yMap == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(yMap.get(chunkMinZ));
    }
}
