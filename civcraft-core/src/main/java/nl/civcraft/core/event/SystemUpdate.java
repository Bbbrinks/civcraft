package nl.civcraft.core.event;

import nl.civcraft.core.CivCraftAppState;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 2-4-2016.
 * <p>
 * This is probably not worth documenting
 */
public class SystemUpdate extends ApplicationEvent {
    private final float tpf;

    public SystemUpdate(float tpf, CivCraftAppState systemEventHandler) {
        super(systemEventHandler);
        this.tpf = tpf;
    }

    public float getTpf() {
        return tpf;
    }
}
