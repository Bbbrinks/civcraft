package nl.civcraft.core.worldgeneration;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.rendering.RenderedVoxelFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkRendererControl extends AbstractControl {

    private static final Logger LOGGER = LogManager.getLogger();

    private Chunk chunk;
    private final List<RenderedVoxelFilter> voxelFilters;

    public ChunkRendererControl(List<RenderedVoxelFilter> voxelFilters) {
        this.voxelFilters = voxelFilters;
    }


    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Chunk)) {
            throw new IllegalArgumentException("ChunkRendererControl can only be attached to Chunk!");
        }
        this.chunk = (Chunk) spatial;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (!chunk.isOptimized() && !chunk.isOptimizing()) {
            ChunkOptimizer chunkOptimizer = new ChunkOptimizer(voxelFilters, chunk);
            Thread optimizeThread = new Thread(chunkOptimizer);
            optimizeThread.start();
        }
        if (chunk.isOptimizingDone()){
            chunk.updateVoxelCache();
            chunk.setOptimizingDone(false);
        }
    }




    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

}
