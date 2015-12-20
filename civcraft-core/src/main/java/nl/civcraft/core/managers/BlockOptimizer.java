package nl.civcraft.core.managers;

import com.jme3.scene.Spatial;
import nl.civcraft.core.model.Voxel;

import java.util.List;

/**
 * Created by Bob on 20-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public interface BlockOptimizer {
    boolean canMerge(Voxel voxel, Voxel other);

    Spatial optimize(List<Voxel> voxels);
}
