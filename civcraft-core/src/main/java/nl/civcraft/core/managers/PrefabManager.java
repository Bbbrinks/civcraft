package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.gamecomponents.ManagedObject;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.GameObjectChangedEvent;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import nl.civcraft.core.model.events.GameObjectDestroyedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 18-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class PrefabManager {
    private final List<GameObject> managedObjects;
    private final List<GameComponent.GameComponentFactory> gameComponents;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PrefabManager parent;

    public PrefabManager(ApplicationEventPublisher applicationEventPublisher, PrefabManager parent) {
        this.parent = parent;
        managedObjects = new ArrayList<>();
        gameComponents = new ArrayList<>();
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public GameObject build(Transform transform, boolean publish) {
        GameObject gameObject;
        if (parent != null) {
            gameObject = parent.build(transform, false);
        } else {
            gameObject = new GameObject(transform);
        }
        for (GameComponent.GameComponentFactory gameComponent : gameComponents) {
            gameObject.addComponent(gameComponent.build());
        }
        gameObject.addComponent(new ManagedObject(this));
        managedObjects.add(gameObject);
        if (publish) {
            applicationEventPublisher.publishEvent(new GameObjectCreatedEvent(gameObject, this));
        }
        return gameObject;
    }

    public void registerComponent(GameComponent.GameComponentFactory componentFactory) {
        gameComponents.add(componentFactory);
        for (GameObject managedObject : managedObjects) {
            managedObject.addComponent(componentFactory.build());
        }
    }

    public void unRegisterComponent(GameComponent.GameComponentFactory componentFactory) {
        gameComponents.remove(componentFactory);
        for (GameObject managedObject : managedObjects) {
            @SuppressWarnings("unchecked") Optional<GameComponent> component = managedObject.getComponent(componentFactory.getComponentType());
            component.ifPresent(managedObject::removeComponent);
        }
    }

    public void destroy(GameObject gameObject) {
        applicationEventPublisher.publishEvent(new GameObjectDestroyedEvent(gameObject, this));
    }

    public void changed(GameObject gameObject) {
        applicationEventPublisher.publishEvent(new GameObjectChangedEvent(gameObject, this));
    }

    public <T extends GameComponent> Optional<GameObject> getClosestGameObject(Transform transform, Class<T> stockpileClass) {
        Optional<GameObject> closest = managedObjects.stream().
                filter(o -> o.hasComponent(stockpileClass)).
                sorted((first, second) -> (int) (second.getTransform().getTranslation().distance(transform.getTranslation()) - first.getTransform().getTranslation().distance(transform.getTranslation()))).
                findFirst();
        if (closest.isPresent()) {
            return closest;
        }
        if (parent != null) {
            return parent.getClosestGameObject(transform, GameComponent.class);
        }
        return Optional.empty();
    }
}
