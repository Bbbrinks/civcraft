package nl.civcraft.core.worldgeneration;

import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Voxel;
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
        List<Voxel> unoptimizedVoxels = Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).filter(Voxel::isVisible).collect(Collectors.toList());
        List<List<Voxel>> optimizedVoxels = optimzeVoxels(unoptimizedVoxels);
        List<Spatial> optimizedSpatials = new ArrayList<>();
        for (List<Voxel> optimizedVoxel : optimizedVoxels) {
            optimizedSpatials.add(buildOpitmizedVoxelMesh(optimizedVoxel));
        }
        chunk.setOptimizedVoxels(optimizedSpatials);
        chunk.setOptimizing(false);
        chunk.setOptimizingDone(true);
    }

    private List<List<Voxel>> optimzeVoxels(List<Voxel> unoptimizedVoxels) {
        DebugStatsState.LAST_MESSAGE = "Start optimizing chunk";
        LOGGER.info(DebugStatsState.LAST_MESSAGE);
        List<List<Voxel>> optimizedVoxelGroups = new ArrayList<>();
        while (unoptimizedVoxels.size() > 0) {
            DebugStatsState.LAST_MESSAGE = "Unoptimized voxels left: " + unoptimizedVoxels.size();
            LOGGER.trace(DebugStatsState.LAST_MESSAGE);
            Voxel toBeOptimized = unoptimizedVoxels.get(0);
            List<Voxel> optimizedVoxel = new ArrayList<>();
            optimizedVoxel.add(toBeOptimized);
            getOptimizedVoxel(toBeOptimized, optimizedVoxel, unoptimizedVoxels);
            unoptimizedVoxels.removeAll(optimizedVoxel);
            optimizedVoxelGroups.add(optimizedVoxel);
        }
        DebugStatsState.LAST_MESSAGE = "End optimizing chunk";
        LOGGER.info(DebugStatsState.LAST_MESSAGE);
        return optimizedVoxelGroups;
    }

    private void getOptimizedVoxel(Voxel toBeOptimized, List<Voxel> optimizedVoxel, List<Voxel> unoptimizedVoxels) {
        List<Voxel> mergableNeighbours = toBeOptimized.getNeighbours().stream().filter(v -> unoptimizedVoxels.contains(v)).filter(Voxel::isVisible).filter(v -> toBeOptimized.getBlock().getBlockOptimizer().canMerge(v, toBeOptimized)).filter(v -> !optimizedVoxel.contains(v)).collect(Collectors.toList());
        optimizedVoxel.addAll(mergableNeighbours);
        for (Voxel mergableNeighbour : mergableNeighbours) {
            getOptimizedVoxel(mergableNeighbour, optimizedVoxel, unoptimizedVoxels);
        }
    }

    private Spatial buildOpitmizedVoxelMesh(List<Voxel> optimizedVoxel) {

        Spatial optimize = optimizedVoxel.get(0).getBlock().getBlockOptimizer().optimize(optimizedVoxel);
        optimize.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return optimize;

    }
}
