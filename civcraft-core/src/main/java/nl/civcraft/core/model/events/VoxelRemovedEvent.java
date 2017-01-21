package nl.civcraft.core.model.events;

import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.Chunk;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 4-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelRemovedEvent extends ApplicationEvent {
    private final Voxel voxel;

    public VoxelRemovedEvent(Voxel voxel, Chunk chunk) {
        super(chunk);
        this.voxel = voxel;
    }

    public Voxel getVoxel() {
        return voxel;
    }
}
