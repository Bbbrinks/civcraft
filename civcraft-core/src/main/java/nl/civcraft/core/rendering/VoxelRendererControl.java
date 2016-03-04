package nl.civcraft.core.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import javafx.util.Pair;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.events.VoxelsAddedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class VoxelRendererControl extends AbstractControl {

    private final List<RenderedVoxelFilter> voxelFilters;
    private final Node chunks;
    private Queue<Pair<Chunk, Spatial>> newSpatials;
    private List<Voxel> renderedVoxels;

    @Autowired
    public VoxelRendererControl(List<RenderedVoxelFilter> voxelFilters, Node chunks) {
        this.voxelFilters = voxelFilters;
        this.chunks = chunks;
        newSpatials = new ConcurrentLinkedQueue<>();
        renderedVoxels = new ArrayList<>();
    }


    @EventListener
    public void handleVoxelAdded(VoxelsAddedEvent voxelsAddedEvent) {
        List<Voxel> voxels = voxelsAddedEvent.getVoxels();
        for (RenderedVoxelFilter voxelFilter : voxelFilters) {
            voxels = voxelFilter.filter(voxels);
        }
        for (Voxel voxel : voxels) {

            if (renderedVoxels.contains(voxel) || renderedVoxels.size() > 1) {
                continue;
            }
            Block block = voxel.cloneBlock();
            Pair<Spatial, Collection<Voxel>> optimize = block.getBlockOptimizer().optimize(voxel);

            renderedVoxels.addAll(optimize.getValue());
            newSpatials.add(new Pair<>(voxel.getChunk(), optimize.getKey()));
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        List<Pair<Chunk, Spatial>> failedVoxels = new ArrayList<>();

        int voxelsAdded = 0;
        while (newSpatials.peek() != null && voxelsAdded < 100) {
            Pair<Chunk, Spatial> poll = newSpatials.poll();

            Node voxelChunkNode = (Node) chunks.getChild(poll.getKey().getName());
            if (voxelChunkNode == null) {
                failedVoxels.add(poll);
            } else {
                voxelChunkNode.attachChild(poll.getValue());
            }
            voxelsAdded++;
        }
        newSpatials.addAll(failedVoxels);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
