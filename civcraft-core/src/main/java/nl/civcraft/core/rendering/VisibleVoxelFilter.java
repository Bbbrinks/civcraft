package nl.civcraft.core.rendering;

import nl.civcraft.core.model.Voxel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VisibleVoxelFilter implements RenderedVoxelFilter {
    @Override
    public List<Voxel> filter(List<Voxel> unoptimizedVoxels) {
        return unoptimizedVoxels.parallelStream().filter(Voxel::isVisible).collect(Collectors.toList());
    }
}
