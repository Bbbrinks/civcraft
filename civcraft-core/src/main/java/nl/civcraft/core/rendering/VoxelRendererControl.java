package nl.civcraft.core.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import javafx.util.Pair;
import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.model.events.VoxelRemovedEvent;
import nl.civcraft.core.model.events.VoxelsAddedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Component
public class VoxelRendererControl extends AbstractControl {

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<RenderedVoxelFilter> voxelFilters;
    private final Node chunks;
    private Queue<Pair<Chunk, Spatial>> newSpatials;
    private Queue<Chunk> modifiedChunks;

    @Autowired
    public VoxelRendererControl(List<RenderedVoxelFilter> voxelFilters, Node chunks) {
        this.voxelFilters = voxelFilters;
        this.chunks = chunks;
        newSpatials = new ConcurrentLinkedQueue<>();
        modifiedChunks = new ConcurrentLinkedQueue<>();
    }


    @EventListener
    public void handleVoxelsAdded(VoxelsAddedEvent voxelsAddedEvent) {
        List<Voxel> voxels = voxelsAddedEvent.getVoxels();
        renderVoxels(voxels);
    }

    private void renderVoxels(List<Voxel> voxels) {
        for (RenderedVoxelFilter voxelFilter : voxelFilters) {
            voxels = voxelFilter.filter(voxels);
        }
        List<List<Voxel>> lists = optimizeVoxels(voxels);
        for (List<Voxel> optimizedVoxels : lists) {
            Spatial spatial = buildOptimizedVoxelMesh(optimizedVoxels);
            newSpatials.add(new Pair<>(optimizedVoxels.get(0).getChunk(), spatial));
        }
    }

    @EventListener
    public void handleVoxelRemovedEvent(VoxelRemovedEvent voxelRemovedEvent) {
        Chunk chunk = voxelRemovedEvent.getVoxel().getChunk();
        modifiedChunks.add(chunk);
        renderVoxels(Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).collect(Collectors.toList()));
    }

    @Override
    protected void controlUpdate(float tpf) {
        while (modifiedChunks.peek() != null) {
            Node voxelChunkNode = (Node) chunks.getChild(modifiedChunks.poll().getName());
            voxelChunkNode.detachAllChildren();
        }

        List<Pair<Chunk, Spatial>> failedVoxels = new ArrayList<>();

        int voxelsAdded = 0;
        while (newSpatials.peek() != null && voxelsAdded < Math.pow(World.CHUNK_SIZE, 3)) {
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

    private List<List<Voxel>> optimizeVoxels(List<Voxel> unoptimizedVoxels) {
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
        List<Voxel> mergableNeighbours = toBeOptimized.getNeighbours().stream().filter(unoptimizedVoxels::contains).filter(Voxel::isVisible).filter(v -> toBeOptimized.cloneBlock().getBlockOptimizer().canMerge(v, toBeOptimized)).filter(v -> !optimizedVoxel.contains(v)).collect(Collectors.toList());
        optimizedVoxel.addAll(mergableNeighbours);
        for (Voxel mergableNeighbour : mergableNeighbours) {
            getOptimizedVoxel(mergableNeighbour, optimizedVoxel, unoptimizedVoxels);
        }
    }

    private Spatial buildOptimizedVoxelMesh(List<Voxel> optimizedVoxel) {

        return optimizedVoxel.get(0).cloneBlock().getBlockOptimizer().optimize(optimizedVoxel);

    }
}
