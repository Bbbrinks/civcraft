package nl.civcraft.core.model.events;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 26-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Tick extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public Tick(Object source) {
        super(source);
    }
}
