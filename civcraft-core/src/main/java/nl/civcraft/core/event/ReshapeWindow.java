package nl.civcraft.core.event;

import nl.civcraft.core.SystemEventHandler;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 2-4-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ReshapeWindow extends ApplicationEvent {
    public ReshapeWindow(SystemEventHandler systemEventHandler) {
        super(systemEventHandler);
    }
}