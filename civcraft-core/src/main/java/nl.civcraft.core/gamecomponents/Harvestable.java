package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Harvestable extends GameComponent {
    Optional<GameObject> harvest(GameObject harvester);
}
