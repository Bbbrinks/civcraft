package nl.civcraft.core.model.events;

import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.World;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 27-2-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkAddedEvent extends ApplicationEvent {
    private final Chunk chunk;

    public ChunkAddedEvent(Chunk chunk, World world) {
        super(world);
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

}
