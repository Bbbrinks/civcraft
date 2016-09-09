package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface GameComponent {
    void addTo(GameObject gameObject);

    void destroyed(GameObject gameObject);
}
