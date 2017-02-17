package nl.civcraft.core.gamecomponents;

import com.jme3.math.Vector3f;
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

    Vector3f getLocation();

    void setLocation(Vector3f location);

}
