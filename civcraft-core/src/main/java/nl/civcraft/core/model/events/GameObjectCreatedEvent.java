package nl.civcraft.core.model.events;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 30-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GameObjectCreatedEvent {
    private final GameObject gameObject;

    public GameObjectCreatedEvent(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
