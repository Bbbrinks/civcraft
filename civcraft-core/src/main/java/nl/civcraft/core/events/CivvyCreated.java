package nl.civcraft.core.events;

import nl.civcraft.core.npc.Civvy;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 8-1-2016.
 * <p>
 * This is probably not worth documenting
 */
public class CivvyCreated extends ApplicationEvent {
    private final Civvy civvy;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public CivvyCreated(Civvy civvy, Object source) {
        super(source);
        this.civvy = civvy;
    }

    public Civvy getCivvy() {
        return civvy;
    }
}
