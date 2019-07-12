package nl.civcraft.core.interaction;

import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 12-7-2019.
 * <p>
 * This is probably not worth documenting
 */
public interface MousePicker {
    <T extends GameComponent> Optional<GameObject> pickNearest(Class<T> gameComponent);
}
