package nl.civcraft.core.worldgeneration;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
            for (int currentDepth = 0; currentDepth < depth; currentDepth++) {
                List<Voxel> toBeOptimized = chunk.descendantMatches(Voxel.class);
                for (Voxel curVoxel : toBeOptimized) {
                    //TODO would be faster to find only neighbours?
                    for (Voxel voxel : toBeOptimized) {
                        LOGGER.debug(String.format("Trying to merge %s and %s", curVoxel, voxel));
                        if (curVoxel.canMerge(voxel)) {
                            LOGGER.debug(String.format("Can be merged %s and %s", curVoxel, voxel));
                            curVoxel.merge(voxel);
                            chunk.detachChild(voxel);
                            LOGGER.debug(String.format("Merge result %s", voxel));
                        }
                    }
                }
            }
            chunk.setOptimized(true);
        }
    }


    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
