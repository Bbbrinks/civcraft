package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Inventory extends Serializable {
    Optional<GameObject> getFirstItem();

    boolean addItem(GameObject item);

    boolean isEmpty();

    void remove(GameObject item);

    boolean hasRoom(GameObject item);

}
