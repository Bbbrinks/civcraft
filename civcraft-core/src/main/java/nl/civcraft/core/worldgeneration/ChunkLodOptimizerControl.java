package nl.civcraft.core.worldgeneration;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkLodOptimizerControl extends AbstractControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Chunk chunk;

    private int depth = 10;
    private boolean optimizing;

    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Chunk)) {
            throw new IllegalArgumentException("ChunkLodOptimizerControl can only be attached to Chunk!");
        }
        this.chunk = (Chunk) spatial;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!chunk.isOptimized() && !isOptimizing()) {
            optimizing = true;
            LOGGER.info(String.format("Starting chunk optimization: %s", chunk));
            List<Voxel> unoptimizedVoxels = Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).collect(Collectors.toList());
            List<List<Voxel>> optimizedVoxels = optimzeVoxels(unoptimizedVoxels);
            chunk.setOptimizedVoxels(optimizedVoxels);
            chunk.updateVoxelCache();
            optimizing = false;
        }
    }

    private List<List<Voxel>> optimzeVoxels(List<Voxel> unoptimizedVoxels) {
        LOGGER.info("Optimizing chunk");
        List<List<Voxel>> optimizedVoxelGroups = new ArrayList<>();
        while (unoptimizedVoxels.size() > 0){
            LOGGER.trace("Unoptimized voxels left: " + unoptimizedVoxels.size());
            Voxel toBeOptimized = unoptimizedVoxels.get(0);
            List<Voxel> optimizedVoxel= new ArrayList<>();
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


    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    public boolean isOptimizing() {
        return optimizing;
    }

    public void setOptimizing(boolean optimizing) {
        this.optimizing = optimizing;
    }
}
