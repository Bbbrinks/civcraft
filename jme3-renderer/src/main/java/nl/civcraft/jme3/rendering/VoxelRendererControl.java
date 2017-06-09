package nl.civcraft.jme3.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.jme3.gamecomponents.VoxelRenderer;
import nl.civcraft.jme3.utils.BlockUtil;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.*;

public class VoxelRendererControl extends AbstractControl {

    private static final int MAX_CHUNKS_PER_CYCLE = 2;
    private final Node chunks;
    private final ChunkOptimizer chunkOptimizer;
    private final ExecutorService executorService;
    private final Queue<Future<ChunkOptimizer.ChunkOptimizerThread>> newOptimizedChunks;
    private final Map<Chunk, Future<ChunkOptimizer.ChunkOptimizerThread>> optimizerThreadMap;
    private final VoxelManager voxelManager;

    @Inject
    public VoxelRendererControl(@Named("chunks") Node chunks,
                                ChunkOptimizer chunkOptimizer) {
        optimizerThreadMap = new HashMap<>();
        newOptimizedChunks = new LinkedBlockingQueue<>();
        this.chunkOptimizer = chunkOptimizer;
        this.chunks = chunks;
        chunks.addControl(this);
        executorService = Executors.newFixedThreadPool(4);
        voxelManager = new VoxelManager();
    }


    public void handleVoxelAdded(VoxelRenderer component) {
        voxelManager.addVoxel(component.getGameObject());
        Chunk chunk = voxelManager.getChunkAt(component.getGameObject()).map(c -> c).orElseThrow(() -> new IllegalStateException("Chunk not foudn"));
        if (optimizerThreadMap.containsKey(chunk)) {
            optimizerThreadMap.get(chunk).cancel(true);
            newOptimizedChunks.remove(optimizerThreadMap.get(chunk));
        }
        Future<ChunkOptimizer.ChunkOptimizerThread> submit = executorService.submit(chunkOptimizer.optimizeChunk(chunk));
        newOptimizedChunks.add(submit);
        optimizerThreadMap.put(chunk, submit);
    }

    public void handleVoxelRemoved(VoxelRenderer component) {
        voxelManager.removeVoxel(component.getGameObject());
        Chunk chunk = voxelManager.getChunkAt(component.getGameObject()).map(c -> c).orElseThrow(() -> new IllegalStateException("Chunk not foudn"));
        if (optimizerThreadMap.containsKey(chunk)) {
            optimizerThreadMap.get(chunk).cancel(true);
            newOptimizedChunks.remove(optimizerThreadMap.get(chunk));
        }
        Future<ChunkOptimizer.ChunkOptimizerThread> submit = executorService.submit(chunkOptimizer.optimizeChunk(chunk));
        newOptimizedChunks.add(submit);
        optimizerThreadMap.put(chunk, submit);
    }

    @Override
    protected void controlUpdate(float tpf) {
        List<Future<ChunkOptimizer.ChunkOptimizerThread>> failed = new ArrayList<>();
        int chunksHandled = 0;
        while (!newOptimizedChunks.isEmpty() && chunksHandled < MAX_CHUNKS_PER_CYCLE) {
            chunksHandled++;
            Future<ChunkOptimizer.ChunkOptimizerThread> next = newOptimizedChunks.poll();
            if (next == null || next.isCancelled()) {
                continue;
            }
            if (!next.isDone()) {
                failed.add(next);
                continue;
            }
            ChunkOptimizer.ChunkOptimizerThread chunkOptimizerThread;
            try {
                chunkOptimizerThread = next.get();
            } catch (InterruptedException | CancellationException e) {
                failed.add(next);
                continue;
            } catch (ExecutionException e) {
                throw new IllegalStateException("Failed to render", e);
            }
            List<Geometry> geometries = chunkOptimizerThread.getGeometries();
            Chunk chunk = chunkOptimizerThread.getChunk();
            Node chunkNode = (Node) chunks.getChild(chunk.getName());
            if (chunkNode == null) {
                chunkNode = new Node(chunk.getName());
                chunks.attachChild(chunkNode);
            }
            chunkNode.setShadowMode(RenderQueue.ShadowMode.Off);
            chunkNode.detachAllChildren();
            for (Geometry geometry : geometries) {
                geometry.setLocalTranslation(chunk.getTransform().getTranslation().getX() - BlockUtil.BLOCK_SIZE, chunk.getTransform().getTranslation().getY() - BlockUtil.BLOCK_SIZE, chunk.getTransform().getTranslation().getZ() - BlockUtil.BLOCK_SIZE);
                chunkNode.attachChild(geometry);
            }
            chunkNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        }
        newOptimizedChunks.addAll(failed);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }

    public interface Factory {
        VoxelRendererControl build();
    }
}
