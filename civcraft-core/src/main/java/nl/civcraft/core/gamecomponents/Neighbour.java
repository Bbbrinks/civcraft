package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Neighbour extends AbstractGameComponent {

    private final Map<Face, GameObject> neighbours;

    public Neighbour() {
        neighbours = new HashMap<>();
    }

    public static Map<Face, GameObject> getNeighbours(GameObject gameObject) {
        return gameObject.getComponent(Neighbour.class).
                map(Neighbour::getNeighbours).
                orElse(Collections.emptyMap());
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
    }

    @Override
    public void destroyed(GameObject gameObject) {
        super.destroyed(gameObject);
    }

    public Map<Face, GameObject> getNeighbours() {
        return neighbours;
    }

    //TODO move this out of Neighbour class
    public List<GameObject> getEnterableNeighbours() {
        List<GameObject> enterableNeighbours = neighbours.entrySet().stream().
                filter(v -> !hasNeighbour(v.getValue(), Face.TOP)).
                map(Map.Entry::getValue).collect(Collectors.toList());
        List<GameObject> verticalDiagonals = new ArrayList<>();
        for (GameObject enterableNeighbour : getNeighbours(Face.BACK, Face.FRONT, Face.LEFT, Face.RIGHT)) {
            if (enterableNeighbour != null) {
                Optional<GameObject> neighbour = getNeighbour(enterableNeighbour, Face.TOP);
                if (neighbour.isPresent() && !hasNeighbour(neighbour.get(), Face.TOP)) {
                    verticalDiagonals.add(neighbour.get());
                }
            }
        }
        Optional<GameObject> bottom = getNeighbour(Face.BOTTOM);
        if (bottom.isPresent()) {
            for (GameObject neighbour : getNeighbours(bottom.get(), Face.BACK, Face.FRONT, Face.LEFT, Face.RIGHT)) {
                if (neighbour != null && !getNeighbour(neighbour, Face.TOP).isPresent()) {
                    verticalDiagonals.add(neighbour);
                }
            }
        }
        enterableNeighbours.addAll(verticalDiagonals);
        return enterableNeighbours;
    }

    public static Boolean hasNeighbour(GameObject gameObject, Face face) {
        return gameObject.
                getComponent(Neighbour.class).
                map(neighbourComponent -> neighbourComponent.getNeighbour(face).isPresent()).
                orElse(false);
    }

    private List<GameObject> getNeighbours(Face... faces) {
        List<GameObject> neighbours = new ArrayList<>();
        for (Face face : faces) {
            Optional<GameObject> neighbour = getNeighbour(face);
            if (neighbour.isPresent()) {
                neighbours.add(neighbour.get());
            }
        }
        return neighbours;
    }

    public static Optional<GameObject> getNeighbour(GameObject gameObject, Face face) {
        return gameObject.getComponent(Neighbour.class).
                map(n -> n.getNeighbour(face)).
                orElse(Optional.empty());
    }

    public Optional<GameObject> getNeighbour(Face face) {
        return Optional.ofNullable(neighbours.get(face));
    }

    public static List<GameObject> getNeighbours(GameObject gameObject, Face... faces) {
        return gameObject.getComponent(Neighbour.class).
                map(n -> n.getNeighbours(faces)).
                orElse(Collections.emptyList());
    }

    public void addNeighbour(Face face, GameObject gameObject) {
        boolean isNew = !neighbours.containsValue(gameObject);
        neighbours.put(face, gameObject);
        if (isNew) {
            gameObject.getComponent(Neighbour.class).ifPresent(n -> n.addNeighbour(face.getOpposite(), this.getGameObject()));
        }
    }

    public void removeNeighbour(GameObject gameObject) {
        boolean removed = neighbours.entrySet().removeIf(entry -> entry.getValue().equals(gameObject));
        if (removed) {
            gameObject.getComponent(Neighbour.class).ifPresent(n -> n.removeNeighbour(this.getGameObject()));
        }
    }

    public void remove() {
        for (GameObject neighbour : neighbours.values()) {
            neighbour.getComponent(Neighbour.class).ifPresent(n -> removeNeighbour(this.getGameObject()));
        }
    }

    @Component
    public static class Factory implements GameComponentFactory<Neighbour> {

        @Override
        public Neighbour build() {
            return new Neighbour();
        }

        @Override
        public Class<Neighbour> getComponentType() {
            return Neighbour.class;
        }
    }
}
