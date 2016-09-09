package nl.civcraft.core.model.events;

import nl.civcraft.core.model.GameObject;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class EntityCreatedEvent extends ApplicationEvent {
    private final GameObject entity;

    /**
     * Create a new ApplicationEvent.
     *
     * @param gameObject
     * @param source     the object on which the event initially occurred (never {@code null})
     */
    public EntityCreatedEvent(GameObject gameObject, Object source) {
        super(source);
        this.entity = gameObject;
    }

    public GameObject getEntity() {
        return entity;
    }
}
