package nl.civcraft.core.model.events;

import nl.civcraft.core.model.GameObject;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class GameObjectDestroyedEvent extends ApplicationEvent {

    private final GameObject gameObject;

    public GameObjectDestroyedEvent(GameObject gameObject, Object source) {
        super(source);
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

}
