package nl.civcraft.core.managers;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.gamecomponents.ManagedObject;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by Bob on 18-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class PrefabManager {
    private final List<GameObject> managedObjects;
    private final List<GameComponent.GameComponentFactory> gameComponents;
    private final PrefabManager parent;
    private final List<PrefabManager> children;


    private final Subject<GameObject> gameObjectCreated;
    private final Subject<GameObject> gameObjectDestroyed;
    private final Subject<GameObject> gameObjectChanged;

    private boolean publishingPaused = false;
    private final Set<GameObject> createdGameObjects;
    private final Set<GameObject> changedGameObjects;
    private final Set<GameObject> destroyedGameObjects;

    public PrefabManager() {
        this(null);
    }

    public PrefabManager(PrefabManager parent) {
        this.parent = parent;
        if (parent != null) {
            parent.children.add(this);
        }
        managedObjects = new ArrayList<>();
        gameComponents = new ArrayList<>();
        gameObjectCreated = PublishSubject.create();
        gameObjectDestroyed = PublishSubject.create();
        gameObjectChanged = PublishSubject.create();
        createdGameObjects = new HashSet<>();
        changedGameObjects = new HashSet<>();
        destroyedGameObjects = new HashSet<>();
        children = new ArrayList<>();
    }

    public GameObject build(Matrix4f transform,
                            boolean publish) {
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
            publishCreatedWithParents(gameObject);
        }
        return gameObject;
    }

    private void publishCreatedWithParents(GameObject gameObject) {
        if (!publishingPaused) {
            gameObjectCreated.onNext(gameObject);
            if (parent != null) {
                parent.publishCreatedWithParents(gameObject);
            }
        } else {
            createdGameObjects.add(gameObject);
        }
    }

    private void publishChangedWithParents(GameObject gameObject) {
        if (!publishingPaused) {
            gameObjectChanged.onNext(gameObject);
            if (parent != null) {
                parent.publishChangedWithParents(gameObject);
            }
        } else {
            changedGameObjects.add(gameObject);
        }
    }

    private void publishDestroyedWithParents(GameObject gameObject) {
        if (!publishingPaused) {
            gameObjectDestroyed.onNext(gameObject);
            if (parent != null) {
                parent.publishDestroyedWithParents(gameObject);
            }
        } else {
            destroyedGameObjects.add(gameObject);
        }
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

    public void pausePublishing() {
        publishingPaused = true;
        for (PrefabManager child : children) {
            child.pausePublishing();
        }
    }

    public void resumePublishing() {
        publishingPaused = false;
        createdGameObjects.forEach(this::publishCreatedWithParents);
        changedGameObjects.forEach(this::publishChangedWithParents);
        destroyedGameObjects.forEach(this::publishDestroyedWithParents);
        for (PrefabManager child : children) {
            child.resumePublishing();
        }
    }

    public void destroy(GameObject gameObject) {
        publishDestroyedWithParents(gameObject);
    }

    public void changed(GameObject gameObject) {
        publishChangedWithParents(gameObject);
    }

    public <T extends GameComponent> Optional<GameObject> getClosestGameObject(Matrix4f transform,
                                                                               Class<T> withComponent) {
        return getClosestGameObject(transform, o -> o.hasComponent(withComponent));
    }

    public Optional<GameObject> getClosestGameObject(Matrix4f transform,
                                                     Predicate<GameObject> predicate) {
        Optional<GameObject> closest = managedObjects.stream().
                filter(predicate).
                sorted((first, second) -> {
                    Vector3f targetTranslation = transform
                            .getTranslation(new Vector3f());
                    return (int) (second.getTransform().getTranslation(new Vector3f()).distance(targetTranslation) - first.getTransform().getTranslation(new Vector3f()).distance(targetTranslation));
                }).
                findFirst();
        if (closest.isPresent()) {
            return closest;
        }
        if (parent != null) {
            return parent.getClosestGameObject(transform, GameComponent.class);
        }
        return Optional.empty();
    }

    public Subject<GameObject> getGameObjectCreated() {
        return gameObjectCreated;
    }

    public Subject<GameObject> getGameObjectDestroyed() {
        return gameObjectDestroyed;
    }

    public Subject<GameObject> getGameObjectChanged() {
        return gameObjectChanged;
    }

    public <T extends GameComponent.GameComponentFactory> Optional<T> getComponentFactory(Class<T> componentFactoryClass) {
        Optional<T> first = gameComponents.stream()
                .filter(gameComponentFactory -> componentFactoryClass.isAssignableFrom(gameComponentFactory.getClass()))
                .map(componentFactoryClass::cast)
                .findFirst();
        if (first.isPresent()) {
            return first;
        } else if (parent != null) {
            return parent.getComponentFactory(componentFactoryClass);
        }
        return Optional.empty();
    }

    public List<GameObject> getManagedObjects() {
        return managedObjects;
    }
}
