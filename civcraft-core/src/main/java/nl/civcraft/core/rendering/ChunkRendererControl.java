package nl.civcraft.core.rendering;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.model.events.ChunkAddedEvent;
import nl.civcraft.core.model.events.ChunkModifiedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ChunkRendererControl extends AbstractControl {


    private static final Logger LOGGER = LogManager.getLogger();
    private Node chunks;
    private Queue<Node> newChunks;


    public ChunkRendererControl() {
        newChunks = new ConcurrentLinkedQueue<>();
    }


    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Node)) {
            throw new IllegalArgumentException("ChunkRendererControl can only be attached to Node!");
        }
        this.chunks = (Node) spatial;
    }

    @Override
    protected void controlUpdate(float tpf) {
        while (newChunks.peek() != null) {
            Node newChunkNode = newChunks.poll();
            chunks.detachChildNamed(newChunkNode.getName());
            chunks.attachChild(newChunkNode);
        }
    }





    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @EventListener
    public void handleChunkAddedEvent(ChunkAddedEvent chunkAddedEvent) {
        Node chunkNode = new Node(chunkAddedEvent.getChunk().getName());
        newChunks.add(chunkNode);
    }

    @EventListener
    public void handleChunkModifiedEvent(ChunkModifiedEvent chunkModifiedEvent) {

    }
}
