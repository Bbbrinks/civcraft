package nl.civcraft.core.conf;

import com.jme3.scene.Node;
import nl.civcraft.core.rendering.ChunkRendererControl;
import nl.civcraft.core.rendering.RenderedVoxelFilter;
import nl.civcraft.core.rendering.VoxelRendererControl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RenderingConfiguration {

    @Bean
    public Node chunks(Node rootNode) {
        rootNode.detachChildNamed("chunks");
        Node chunks = new Node("chunks");
        rootNode.attachChild(chunks);
        return chunks;
    }

    @Bean
    public ChunkRendererControl chunkRendererControl(Node chunks) {
        ChunkRendererControl chunkRendererControl = new ChunkRendererControl();
        chunks.addControl(chunkRendererControl);
        return chunkRendererControl;
    }

    @Bean
    public VoxelRendererControl voxelRendererControl(List<RenderedVoxelFilter> voxelFilters, Node chunks) {
        VoxelRendererControl voxelRendererControl = new VoxelRendererControl(voxelFilters, chunks);
        chunks.addControl(voxelRendererControl);
        return voxelRendererControl;
    }
}
