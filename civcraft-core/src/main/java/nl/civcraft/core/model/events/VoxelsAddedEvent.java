package nl.civcraft.core.model.events;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * Created by Bob on 27-2-2016.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelsAddedEvent extends ApplicationEvent {


    private final List<Voxel> voxels;

    public VoxelsAddedEvent(List<Voxel> voxels, World world) {
        super(world);
        this.voxels = voxels;
    }

    public List<Voxel> getVoxels() {
        return voxels;
    }
}
