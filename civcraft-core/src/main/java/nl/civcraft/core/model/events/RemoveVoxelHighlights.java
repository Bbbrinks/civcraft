package nl.civcraft.core.model.events;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class RemoveVoxelHighlights extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public RemoveVoxelHighlights(Object source) {
        super(source);
    }
}
