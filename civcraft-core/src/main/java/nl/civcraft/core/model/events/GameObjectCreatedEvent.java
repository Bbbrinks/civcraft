package nl.civcraft.core.model.events;

import nl.civcraft.core.model.GameObject;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 30-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GameObjectCreatedEvent extends ApplicationEvent {
    private final GameObject gameObject;

    public GameObjectCreatedEvent(GameObject gameObject, Object source) {
        super(source);
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
