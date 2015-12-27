package nl.civcraft.core.rendering;

import nl.civcraft.core.model.Voxel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 26-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class VissibleVoxelFilter implements  RenderedVoxelFilter {
    @Override
    public List<Voxel> filter(List<Voxel> unoptimizedVoxels) {
        return unoptimizedVoxels.parallelStream().filter(Voxel::isVisible).collect(Collectors.toList());
    }
}
