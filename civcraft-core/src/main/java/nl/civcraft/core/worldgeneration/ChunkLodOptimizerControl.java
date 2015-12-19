package nl.civcraft.core.worldgeneration;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.model.Face;
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

    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Chunk)) {
            throw new IllegalArgumentException("ChunkLodOptimizerControl can only be attached to Chunk!");
        }
        this.chunk = (Chunk) spatial;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!chunk.isOptimized()) {
            LOGGER.info(String.format("Starting chunk optimization: %s", chunk));
            List<Voxel> unoptimizedVoxels = Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).collect(Collectors.toList());
            optimzeVoxels(unoptimizedVoxels);
            chunk.setOptimized(true);
        }
    }

    private void optimzeVoxels(List<Voxel> unoptimizedVoxels) {
        List<Voxel> optimizedThisRun = new ArrayList<>();
        Voxel toBeOptimized = unoptimizedVoxels.get(0);
        optimizedThisRun.add(toBeOptimized);
        int originX = toBeOptimized.getX();
        int originY = toBeOptimized.getY();
        int originZ = toBeOptimized.getZ();
        int originWidth = 1;
        int originHeight = 1;
        int originDepth = 1;
        boolean stillMerging = true;
        Voxel currentlyOptimizing = toBeOptimized;
        while (stillMerging) {
            Voxel rightNeighbour = chunk.getNeighbour(currentlyOptimizing, Face.RIGHT);
            if (currentlyOptimizing.canMerge(rightNeighbour)) {
                originWidth++;
                optimizedThisRun.add(rightNeighbour);
                currentlyOptimizing = rightNeighbour;
            } else {
                stillMerging = false;
            }
        }
        stillMerging = true;
        currentlyOptimizing = chunk.getNeighbour(toBeOptimized, Face.BACK);
        while (stillMerging) {
            boolean canMerge = true;
            List<Voxel> candidates = new ArrayList<>();
            for (int i = 0; i < originWidth; i++) {
                Voxel neighbour = chunk.getNeighbour(currentlyOptimizing, Face.RIGHT);
                candidates.add(neighbour);
                if (!currentlyOptimizing.canMerge(neighbour)) {
                    canMerge = false;
                    break;
                }
                currentlyOptimizing = neighbour;
            }
            if (canMerge) {
                optimizedThisRun.addAll(candidates);
                originDepth++;
            } else {
                stillMerging = false;
            }
        }
        stillMerging = true;
        currentlyOptimizing = chunk.getNeighbour(toBeOptimized, Face.DOWN);
        while (stillMerging) {
            boolean canMerge = true;
            List<Voxel> candidates = new ArrayList<>();
            for (int i = 0; i < originWidth; i++) {
                Voxel rightNeighbour = chunk.getNeighbour(currentlyOptimizing, Face.RIGHT);
                candidates.add(rightNeighbour);
                if (!currentlyOptimizing.canMerge(rightNeighbour)) {
                    canMerge = false;
                    break;
                }
                for (int j = 0; j < originDepth; j++) {
                    Voxel voxelBottomNeighbour = chunk.getNeighbour(rightNeighbour, Face.BACK);

                    vo

                }
                currentlyOptimizing = rightNeighbour;
            }
            if (canMerge) {
                optimizedThisRun.addAll(candidates);
                originHeight++;
            } else {
                stillMerging = false;
            }
        }
    }


    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
