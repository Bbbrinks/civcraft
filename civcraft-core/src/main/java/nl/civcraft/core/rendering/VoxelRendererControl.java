package nl.civcraft.core.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import javafx.util.Pair;
import jme3tools.optimize.GeometryBatchFactory;
import nl.civcraft.core.gamecomponents.VoxelRenderer;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.model.events.VoxelChangedEvent;
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
        List<VoxelRenderer> voxelRenderers = voxels.stream().map(v -> v.getGameObject().getComponent(VoxelRenderer.class).get()).collect(Collectors.toList());
        Node optimize = optimize(voxelRenderers);
        if (!voxelRenderers.isEmpty()) {
            newSpatials.add(new Pair<>(voxelRenderers.get(0).getVoxel().getChunk(), optimize));
        }
    }

    private Node optimize(List<VoxelRenderer> voxelRenders) {
        List<Geometry> allGeometries = new ArrayList<>();
        for (VoxelRenderer voxelRenderer : voxelRenders) {
            List<Geometry> geometries = voxelRenderer.getNode().descendantMatches(Geometry.class);
            for (Geometry geometry : geometries) {
                geometry.setLocalTranslation(geometry.getLocalTranslation().add(voxelRenderer.getVoxel().getX(), voxelRenderer.getVoxel().getY(), voxelRenderer.getVoxel().getZ()));
                allGeometries.add(geometry);
            }
        }
        List<Geometry> batches = GeometryBatchFactory.makeBatches(allGeometries);
        Node optimizedVoxels = new Node();
        for (Geometry batch : batches) {
            batch.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
            optimizedVoxels.attachChild(batch);
        }
        return optimizedVoxels;
    }

    @EventListener
    public void handleVoxelChanged(VoxelChangedEvent voxelChangedEvent) {
        Chunk chunk = voxelChangedEvent.getVoxel().getChunk();
        modifiedChunks.add(chunk);
        // renderVoxels(Arrays.asList(chunk.getVoxels()).stream().filter(v -> v != null).collect(Collectors.toList()));
    }

    @EventListener
    public void handleVoxelRemovedEvent(VoxelRemovedEvent voxelRemovedEvent) {
        Chunk chunk = voxelRemovedEvent.getVoxel().getChunk();
        if (!modifiedChunks.contains(chunk)) {
            modifiedChunks.add(chunk);
        }

    }

    @Override
    protected void controlUpdate(float tpf) {
        while (modifiedChunks.peek() != null) {
            Chunk chunk = modifiedChunks.poll();
            Node voxelChunkNode = (Node) chunks.getChild(chunk.getName());
            voxelChunkNode.detachAllChildren();
            renderVoxels(Arrays.stream(chunk.getVoxels()).filter(v -> v != null).collect(Collectors.toList()));
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
}
