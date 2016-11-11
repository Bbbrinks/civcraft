package nl.civcraft.jme3.conf;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import nl.civcraft.jme3.Civcraft;
import nl.civcraft.jme3.rendering.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public VoxelRendererControl voxelRendererControl(Node chunks, ChunkOptimizer chunkOptimizer) {
        VoxelRendererControl voxelRendererControl = new VoxelRendererControl(chunks, chunkOptimizer);
        chunks.addControl(voxelRendererControl);
        return voxelRendererControl;
    }

    @Bean
    public CivvyControl civvyControl(Node rootNode, Node civvy) {
        CivvyControl control = new CivvyControl(rootNode, civvy);
        rootNode.addControl(control);
        return control;
    }

    @Bean
    public VoxelHighlightControl voxelHighlightControl(Spatial hoverSpatial, Node selectionBoxes, Spatial rootNode) {
        VoxelHighlightControl voxelHighlightControl = new VoxelHighlightControl(selectionBoxes, hoverSpatial);
        rootNode.addControl(voxelHighlightControl);
        return voxelHighlightControl;
    }

    @Bean
    public Node rootNode(Civcraft civcraft) {
        return civcraft.getRootNode();
    }

    @Bean
    public Renderer renderer(JmeContext context) {
        return context.getRenderer();
    }

    @Bean
    public RenderManager renderManager(Civcraft civcraft) {
        return civcraft.getRenderManager();
    }

    @Bean
    public Camera camera(Civcraft civcraft) {

        Camera cam = civcraft.getCamera();

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 1f, 1000f);
        cam.setLocation(new Vector3f(0f, 0f, 10f));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
        return cam;

    }

    @Bean
    public ViewPort mainViewPort(Civcraft civcraft) {
        return civcraft.getViewPort();
    }

    @Bean
    public ViewPort guiViewPort(Civcraft civcraft) {
        return civcraft.getGuiViewPort();
    }
}
