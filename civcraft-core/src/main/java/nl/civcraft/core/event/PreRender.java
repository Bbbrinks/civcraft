package nl.civcraft.core.event;

import nl.civcraft.core.CivCraftApplication;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 2-4-2016.
 * <p>
 * This is probably not worth documenting
 */
public class PreRender extends ApplicationEvent {
    public PreRender(CivCraftApplication civCraftApplication) {
        super(civCraftApplication);
    }
}
