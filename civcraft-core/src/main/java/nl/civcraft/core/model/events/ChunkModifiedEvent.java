package nl.civcraft.core.model.events;

import nl.civcraft.core.model.Chunk;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 27-2-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkModifiedEvent extends ApplicationEvent {
    private final Chunk chunk;

    public ChunkModifiedEvent(Chunk chunk, Object origin) {
        super(origin);
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }
}
