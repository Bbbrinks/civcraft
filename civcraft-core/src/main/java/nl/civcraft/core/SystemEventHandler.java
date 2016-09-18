package nl.civcraft.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class SystemEventHandler {


    private final ApplicationEventPublisher publisher;

    /**
     * @param publisher the global Event publisher
     */
    @Autowired
    public SystemEventHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }
}
