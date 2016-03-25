package nl.civcraft.core.model.events;

import nl.civcraft.core.model.Voxel;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelChangedEvent extends ApplicationEvent {
    private final Voxel voxel;

    public VoxelChangedEvent(Voxel voxel, Object source) {
        super(source);
        this.voxel = voxel;
    }

    public Voxel getVoxel() {
        return voxel;
    }
}
