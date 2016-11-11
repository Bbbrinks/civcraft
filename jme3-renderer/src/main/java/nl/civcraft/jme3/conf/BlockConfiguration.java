package nl.civcraft.jme3.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.model.Face;
import nl.civcraft.jme3.utils.BlockUtil;
import nl.civcraft.jme3.utils.MaterialUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockConfiguration {


    @Bean
    public Spatial stockpileSpatial(AssetManager assetManager) {
        Node stockpile = new Node();
        Geometry topGeometry = BlockUtil.getBlockQuadGeometry(Face.TOP);
        topGeometry.setLocalTranslation(topGeometry.getLocalTranslation().add(new Vector3f(0, 0.001f, 0)));
        Material mat = MaterialUtil.getColoredMaterial(assetManager, new ColorRGBA(0.3f, 0.9f, 0.9f, 0.5f));
        topGeometry.setMaterial(mat);
        stockpile.attachChild(topGeometry);
        return stockpile;
    }

}
