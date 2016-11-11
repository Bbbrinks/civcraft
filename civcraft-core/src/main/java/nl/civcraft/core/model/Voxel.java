package nl.civcraft.core.model;

import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.model.events.VoxelChangedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends AbstractGameComponent {

    private final String type;
    private final int x;
    private final int y;
    private final int z;
    private final Map<Face, Voxel> neighbours;
    private final ApplicationEventPublisher publisher;
    private Chunk chunk;
    private int localX;
    private int localY;
    private int localZ;

    public Voxel(int x, int y, int z, String type, ApplicationEventPublisher publisher) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.publisher = publisher;
        neighbours = new HashMap<>();
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public void breakBlock() {
        chunk.removeVoxel(this);
    }

    public boolean isVisible() {
        return neighbours.size() != 6;
    }

    public void addNeighbour(Face face, Voxel voxel) {
        boolean isNew = !neighbours.containsValue(voxel);
        neighbours.put(face, voxel);
        if (isNew) {
            voxel.addNeighbour(face.getOpposite(), this);
        }
    }

    public void removeNeighbour(Voxel voxel) {
        boolean removed = neighbours.entrySet().removeIf(entry -> entry.getValue().equals(voxel));
        if (removed) {
            voxel.removeNeighbour(voxel);
        }
    }

    public void remove() {
        for (Voxel neighbour : neighbours.values()) {
            neighbour.removeNeighbour(this);
        }
    }

    public Map<Face, Voxel> getNeighbours() {
        return neighbours;
    }

    public Vector3f findLocalChunkTranslation() {
        return new Vector3f(x, y, z).subtract(new Vector3f(chunk.getChunkX() * World.CHUNK_SIZE, 0, chunk.getChunkZ() * World.CHUNK_SIZE));
    }

    @Override
    public String toString() {
        return type + "@" + x + "x" + y + "x" + z + " local " + localX + "x" + localY + "x" + localZ;
    }

    public int getLocalX() {
        return localX;
    }

    public void setLocalX(int localX) {
        this.localX = localX;
    }

    public int getLocalY() {
        return localY;
    }

    public void setLocalY(int localY) {
        this.localY = localY;
    }

    public int getLocalZ() {
        return localZ;
    }

    public void setLocalZ(int localZ) {
        this.localZ = localZ;
    }

    //TODO move this out of Voxel class
    public List<Voxel> getEnterableNeighbours() {
        List<Voxel> enterableNeighbours = neighbours.entrySet().stream().filter(v -> !v.getValue().getNeighbour(Face.TOP).isPresent()).map(Map.Entry::getValue).collect(Collectors.toList());
        List<Voxel> verticalDiagonals = new ArrayList<>();
        for (Voxel enterableNeighbour : getNeighbours(Face.BACK, Face.FRONT, Face.LEFT, Face.RIGHT)) {
            if (enterableNeighbour != null) {
                Optional<Voxel> neighbour = enterableNeighbour.getNeighbour(Face.TOP);
                if (neighbour.isPresent() && !neighbour.get().getNeighbour(Face.TOP).isPresent()) {
                    verticalDiagonals.add(neighbour.get());
                }
            }
        }
        Optional<Voxel> bottom = getNeighbour(Face.BOTTOM);
        if (bottom.isPresent()) {
            for (Voxel neighbour : bottom.get().getNeighbours(Face.BACK, Face.FRONT, Face.LEFT, Face.RIGHT)) {
                if (neighbour != null && !neighbour.getNeighbour(Face.TOP).isPresent()) {
                    verticalDiagonals.add(neighbour);
                }
            }
        }

        enterableNeighbours.addAll(verticalDiagonals);
        return enterableNeighbours;
    }

    public Optional<Voxel> getNeighbour(Face face) {
        return Optional.ofNullable(neighbours.get(face));
    }

    private List<Voxel> getNeighbours(Face... faces) {
        List<Voxel> neighbours = new ArrayList<>();
        for (Face face : faces) {
            Optional<Voxel> neighbour = getNeighbour(face);
            if (neighbour.isPresent()) {
                neighbours.add(neighbour.get());
            }
        }
        return neighbours;
    }

    public Vector3f getLocation() {
        return new Vector3f(x, y, z);
    }

    public String getName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("voxel[");
        stringBuilder.append(x);
        stringBuilder.append(",");
        stringBuilder.append(y);
        stringBuilder.append(",");
        stringBuilder.append(z);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public void changed() {
        publisher.publishEvent(new VoxelChangedEvent(this, this));
    }
}
