package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface GameComponent {
    void addTo(GameObject gameObject);

    GameObject getGameObject();

    void destroyed(GameObject gameObject);

    interface GameComponentFactory<T extends GameComponent> {
        GameComponent build();

        Class<T> getComponentType();
    }
}
