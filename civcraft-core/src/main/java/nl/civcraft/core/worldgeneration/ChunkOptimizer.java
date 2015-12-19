package nl.civcraft.core.worldgeneration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 19-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkOptimizer implements Runnable {
    private final Chunk chunk;

    private static final Logger LOGGER = LogManager.getLogger();

    public ChunkOptimizer(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public void run() {
        chunk.setOptimizing(true);
        LOGGER.info(String.format("Starting chunk optimization: %s", chunk));
        List<Voxel> unoptimizedVoxels = Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).collect(Collectors.toList());
        List<List<Voxel>> optimizedVoxels = optimzeVoxels(unoptimizedVoxels);
        chunk.setOptimizedVoxels(optimizedVoxels);
        chunk.setOptimizing(false);
        chunk.setOptimizingDone(true);
    }

    private List<List<Voxel>> optimzeVoxels(List<Voxel> unoptimizedVoxels) {
        LOGGER.info("Optimizing chunk");
        List<List<Voxel>> optimizedVoxelGroups = new ArrayList<>();
        while (unoptimizedVoxels.size() > 0) {
            LOGGER.trace("Unoptimized voxels left: " + unoptimizedVoxels.size());
            Voxel toBeOptimized = unoptimizedVoxels.get(0);
            List<Voxel> optimizedVoxel = new ArrayList<>();
            optimizedVoxel.add(toBeOptimized);
            getOptimizedVoxel(toBeOptimized, optimizedVoxel);
            unoptimizedVoxels.removeAll(optimizedVoxel);
            optimizedVoxelGroups.add(optimizedVoxel);
        }
        return optimizedVoxelGroups;
    }

    private void getOptimizedVoxel(Voxel toBeOptimized, List<Voxel> optimizedVoxel) {
        List<Voxel> mergableNeighbours = chunk.getVoxelNeighbours(toBeOptimized).stream().filter(toBeOptimized::canMerge).filter(v -> !optimizedVoxel.contains(v)).collect(Collectors.toList());
        optimizedVoxel.addAll(mergableNeighbours);
        for (Voxel mergableNeighbour : mergableNeighbours) {
            getOptimizedVoxel(mergableNeighbour, optimizedVoxel);
        }
    }
}
