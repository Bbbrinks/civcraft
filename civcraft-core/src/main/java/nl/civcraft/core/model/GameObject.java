package nl.civcraft.core.model;

import com.jme3.math.Transform;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.rendering.VoxelRenderer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GameObject implements Serializable {

    private final Transform transform;
    private final List<GameComponent> components;

    public GameObject() {
        this(new Transform());

    }

    public GameObject(Transform transform) {
        this.transform = transform;
        components = new ArrayList<>();
    }

    public void addComponent(GameComponent component) {
        components.add(component);
        component.addTo(this);
    }

    public void changed() {
        for (GameComponent component : components) {
            component.changed();
        }
    }

    public void destroy() {
        for (GameComponent component : components) {
            component.destroyed();
        }
    }

    public void removeComponent(GameComponent gameComponent) {
        gameComponent.removeFrom(this);
        components.remove(gameComponent);
    }

    public Transform getTransform() {
        return transform;
    }

    public void removeComponent(Class<VoxelRenderer> voxelRendererClass) {
        getComponent(voxelRendererClass).ifPresent(this::removeComponent);
    }

    public <T extends GameComponent> Optional<T> getComponent(Class<T> componentType) {
        return components.stream().filter(i -> componentType.isAssignableFrom(i.getClass())).map(componentType::cast).findFirst();
    }

    public <T extends GameComponent> boolean hasComponent(Class<T> componentClass) {
        return getComponent(componentClass).isPresent();
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "transform=" + transform +
                ", components=" + components.stream().map(c -> c.getClass().getSimpleName()).collect(Collectors.joining()) +
                '}';
    }
}
