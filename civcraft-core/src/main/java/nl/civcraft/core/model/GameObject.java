package nl.civcraft.core.model;

import nl.civcraft.core.gamecomponents.GameComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GameObject {

    private List<GameComponent> components;

    public GameObject() {
        components = new ArrayList<>();
    }

    public <T> Optional<T> getComponent(Class<T> componentType) {
        return (Optional<T>) components.stream().filter(i -> componentType.isAssignableFrom(i.getClass())).findFirst();
    }

    public void addComponent(GameComponent component) {
        component.addTo(this);
        components.add(component);
    }

    public void changed() {//no op}
    }

    public void destroy() {
        for (GameComponent component : components) {
            component.destroyed(this);
        }
    }
}
