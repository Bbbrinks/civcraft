package nl.civcraft.jme3.conf;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import com.jme3.system.Timer;
import nl.civcraft.jme3.Civcraft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class Jme3Configuration {
    @Bean
    public JmeContext context(Civcraft civcraft) {
        return civcraft.getContext();
    }

    @Bean
    public Timer timer(JmeContext context) {
        return context.getTimer();
    }

    @Bean
    @Scope("singleton")
    public AssetManager assetManager(Civcraft civcraft) {
        return civcraft.getAssetManager();
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
    public BitmapFont guiFont(AssetManager assetManager) {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }
}
