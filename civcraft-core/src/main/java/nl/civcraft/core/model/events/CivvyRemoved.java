package nl.civcraft.core.model.events;

import nl.civcraft.core.npc.Civvy;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 27-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class CivvyRemoved extends ApplicationEvent {
    private final Civvy civvy;

    public CivvyRemoved(Civvy civvy, Object source) {
        super(source);
        this.civvy = civvy;
    }

    public Civvy getCivvy() {
        return civvy;
    }
}
