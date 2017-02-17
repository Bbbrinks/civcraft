package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
//TODO: maybe merge this with voxels as they are the only objects that have neighbours now?.. Consider that neighbour is used by AStar
public class Neighbour extends AbstractGameComponent {

    private final Map<NeighbourDirection, GameObject> neighbours;
    private final VoxelManager voxelManager;
    public Neighbour(VoxelManager voxelManager) {
        this.voxelManager = voxelManager;
        neighbours = new EnumMap<>(NeighbourDirection.class);
    }

    public static Map<NeighbourDirection, GameObject> getNeighbours(GameObject gameObject) {
        return gameObject.getComponent(Neighbour.class).
                map(Neighbour::getNeighbours).
                orElse(Collections.emptyMap());
    }

    public static Optional<GameObject> getNeighbour(GameObject gameObject, NeighbourDirection neighbourDirection) {
        return gameObject.getComponent(Neighbour.class).
                map(n -> n.getNeighbour(neighbourDirection)).
                orElse(Optional.empty());
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        for (NeighbourDirection neighbourDirection : NeighbourDirection.values()) {
            voxelManager.getVoxelAt(getGameObject().getTransform().getTranslation().add(neighbourDirection.getTranslation())).ifPresent(v -> addNeighbour(neighbourDirection, v));
        }
    }

    @Override
    public void destroyed() {
        neighbours.values().
                forEach(n -> n.getComponent(Neighbour.class).
                        ifPresent(on -> on.removeNeighbour(getGameObject()))
                );
        super.destroyed();
    }

    public Map<NeighbourDirection, GameObject> getNeighbours() {
        return neighbours;
    }

    public List<GameObject> getEnterableNeighbours() {
        return neighbours.entrySet().stream().
                filter(v -> !hasNeighbour(v.getValue(), NeighbourDirection.TOP)).
                map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public static Boolean hasNeighbour(GameObject gameObject, NeighbourDirection neighbourDirection) {
        return gameObject.
                getComponent(Neighbour.class).
                map(neighbourComponent -> neighbourComponent.getNeighbour(neighbourDirection).isPresent()).
                orElse(false);
    }

    public Optional<GameObject> getNeighbour(NeighbourDirection neighbourDirection) {
        return Optional.ofNullable(neighbours.get(neighbourDirection));
    }

    public Map<NeighbourDirection, GameObject> getNeighbours(NeighbourDirection... neighbourDirections) {
        List<NeighbourDirection> neighbourDirections1 = Arrays.asList(neighbourDirections);
        return neighbours.entrySet().stream().
                filter(e -> neighbourDirections1.contains(e.getKey())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void addNeighbour(NeighbourDirection neighbourDirection, GameObject gameObject) {
        boolean isNew = !neighbours.containsValue(gameObject);
        neighbours.put(neighbourDirection, gameObject);
        if (isNew) {
            gameObject.getComponent(Neighbour.class).ifPresent(n -> n.addNeighbour(neighbourDirection.getOpposite(), this.getGameObject()));
        }
    }

    private void removeNeighbour(GameObject gameObject) {
        boolean removed = neighbours.entrySet().removeIf(entry -> entry.getValue().equals(gameObject));
        if (removed) {
            gameObject.getComponent(Neighbour.class).ifPresent(n -> n.removeNeighbour(this.getGameObject()));
        }
    }


    @Component
    public static class Factory implements GameComponentFactory<Neighbour> {

        private final VoxelManager voxelManager;

        @Autowired
        public Factory(VoxelManager voxelManager) {
            this.voxelManager = voxelManager;
        }

        @Override
        public Neighbour build() {
            return new Neighbour(voxelManager);
        }

        @Override
        public Class<Neighbour> getComponentType() {
            return Neighbour.class;
        }
    }
}
