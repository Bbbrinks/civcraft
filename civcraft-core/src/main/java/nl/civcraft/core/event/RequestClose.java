package nl.civcraft.core.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 2-4-2016.
 * <p>
 * This is probably not worth documenting
 */
public class RequestClose extends ApplicationEvent {
    public RequestClose(Object source) {
        super(source);
    }
}
