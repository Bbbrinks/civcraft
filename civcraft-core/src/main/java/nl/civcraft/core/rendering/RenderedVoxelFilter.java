package nl.civcraft.core.rendering;

import nl.civcraft.core.gamecomponents.Voxel;

import java.util.List;

/**
 * Created by Bob on 26-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public interface RenderedVoxelFilter {
    List<Voxel> filter(List<Voxel> unoptimizedVoxels);
}
