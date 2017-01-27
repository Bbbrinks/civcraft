package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.io.Serializable;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface GameComponent extends Serializable {
    void addTo(GameObject gameObject);

    GameObject getGameObject();

    void destroyed();

    @SuppressWarnings("EmptyMethod")
    void changed();

    void removeFrom(GameObject gameObject);

    interface GameComponentFactory<T extends GameComponent> extends Serializable {
        T build();

        Class<T> getComponentType();
    }
}
