package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
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
            Optional<GameComponent> component = managedObject.getComponent(componentFactory.getComponentType());
            if (component.isPresent()) {
                managedObject.removeComponent(component.get());
            }
        }
    }
}
