package nl.civcraft.core.conf;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.Timer;
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

    @Bean
    public Node rootNode() {
        return new Node("Root Node");
    }

    @Bean
    public Renderer renderer(JmeContext context) {
        return context.getRenderer();
    }

    @Bean
    public RenderManager renderManager(Renderer renderer, Timer timer) {
        RenderManager renderManager = new RenderManager(renderer);
        renderManager.setTimer(timer);
        return renderManager;
    }

    @Bean
    public Camera camera(AppSettings settings) {
        Camera cam = new Camera(settings.getWidth(), settings.getHeight());

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 1f, 1000f);
        cam.setLocation(new Vector3f(0f, 0f, 10f));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
        return cam;
    }

    @Bean
    Camera guiCamera(AppSettings settings) {
        return new Camera(settings.getWidth(), settings.getHeight());
    }

    @Bean
    public ViewPort mainViewPort(RenderManager renderManager, Camera camera) {
        ViewPort viewPort = renderManager.createMainView("Default", camera);
        viewPort.setClearFlags(true, true, true);
        return viewPort;
    }

    @Bean
    public ViewPort guiViewPort(RenderManager renderManager, Camera guiCamera) {
        ViewPort guiViewPort = renderManager.createPostView("Gui Default", guiCamera);
        guiViewPort.setClearFlags(false, false, false);
        return guiViewPort;
    }
}
