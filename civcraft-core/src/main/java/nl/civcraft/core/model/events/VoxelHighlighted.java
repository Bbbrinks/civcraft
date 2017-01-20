package nl.civcraft.core.model.events;

import nl.civcraft.core.model.GameObject;
import org.springframework.context.ApplicationEvent;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelHighlighted extends ApplicationEvent {
    private final GameObject voxel;

    public VoxelHighlighted(GameObject voxel, Object source) {
        super(source);
        this.voxel = voxel;
    }

    public GameObject getVoxel() {
        return voxel;
    }
}
