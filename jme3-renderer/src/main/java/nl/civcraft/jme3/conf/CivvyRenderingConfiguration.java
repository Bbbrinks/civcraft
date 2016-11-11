package nl.civcraft.jme3.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Bob on 11-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class CivvyRenderingConfiguration {
    @Bean
    public Node civvy(AssetManager assetManager) {
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setColor("Color", ColorRGBA.Blue);
        return createSingleGeometryBoxCivvy(cobbleMaterial, "civvy");
    }

    private Node createSingleGeometryBoxCivvy(Material cobbleMaterial, String name) {
        Box box = new Box(0.25f, 2.0f, 0.35f);
        Geometry geometry = new Geometry("box", box);
        geometry.setMaterial(cobbleMaterial);
        geometry.setLocalTranslation(0.0f, 1.0f, 0.0f);
        Node block = new Node(name);
        block.attachChild(geometry);
        block.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return block;
    }
}
