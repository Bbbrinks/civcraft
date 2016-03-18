package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LodControl;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeSystem;
import nl.civcraft.core.rendering.RenderedVoxelFilter;
import nl.civcraft.core.rendering.VisibleVoxelFilter;
import nl.civcraft.core.rendering.WorldEdgeVoxelFilter;
import nl.civcraft.core.worldgeneration.ChunkBuilder;
import nl.civcraft.core.worldgeneration.WorldGeneratorState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackageClasses = {WorldGeneratorState.class, WorldGeneration.class, BlockConfiguration.class})
public class InitialConfiguration {


    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ChunkBuilder chunkBuilder() {
        return new ChunkBuilder();
    }

    @Bean
    public Node rootNode() {
        return new Node("Root Node");
    }

    @Bean
    public Node guiNode() {
        return new Node("Gui Node");
    }

    @Bean
    public LodControl lodControl() {
        return new LodControl();
    }

    @Bean
    @Scope("singleton")
    public AssetManager assetManager() {
        return JmeSystem.newAssetManager(
                Thread.currentThread().getContextClassLoader()
                        .getResource("com/jme3/asset/Desktop.cfg"));
    }

    @Bean
    public Spatial selectionSpatial(AssetManager assetManager) {
        return getColoredBlock(assetManager, new ColorRGBA(0.1f, 0.7f, 0.7f, 0.5f));
    }

    private Spatial getColoredBlock(AssetManager assetManager, ColorRGBA value) {
        Material mat = new Material(assetManager,  // Create new material and...
                "Common/MatDefs/Misc/Unshaded.j3md");  // ... specify .j3md file to use (unshaded).
        mat.setColor("Color", value);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        Box box = new Box(0.51f, 0.51f, 0.51f);
        Geometry geometry = new Geometry("selectionBox", box);
        geometry.setMaterial(mat);
        geometry.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geometry;
    }

    @Bean
    public Spatial moveToSpatial(AssetManager assetManager) {
        return getColoredBlock(assetManager, new ColorRGBA(0.5f, 0.3f, 0.2f, 0.5f));
    }

    @Bean
    public Spatial hoverSpatial(AssetManager assetManager) {
        return getColoredBlock(assetManager, new ColorRGBA(0.7f, 0.7f, 0.1f, 0.5f));
    }

    @Bean
    public RenderedVoxelFilter visibleVoxelFilter() {
        return new VisibleVoxelFilter();
    }

    @Bean
    public RenderedVoxelFilter worldEdgeVoxelFilter() {
        return new WorldEdgeVoxelFilter();
    }

    @Bean
    public BitmapFont guiFont(AssetManager assetManager) {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }

}
