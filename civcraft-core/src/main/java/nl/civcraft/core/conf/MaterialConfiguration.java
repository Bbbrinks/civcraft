package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MaterialConfiguration {
    @Autowired
    private AssetManager assetManager;

    @Bean
    public Material cobbleMaterial() {

        return getColoredMaterial(ColorRGBA.Gray);
    }

    private Material getColoredMaterial(ColorRGBA colorRGBA) {
        Material coloredMaterial = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        coloredMaterial.setColor("Color", colorRGBA);
        return coloredMaterial;
    }

    private Material getUnshadedMaterial(String name) {
        Texture cobbleTexture = assetManager.loadTexture(
                name);
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setTexture("ColorMap", cobbleTexture);
        return cobbleMaterial;
    }

    @Bean
    public Material dirtMaterial() {
        return getColoredMaterial(ColorRGBA.Brown);
    }

    @Bean
    public Material grassTopMaterial() {
        return getColoredMaterial(ColorRGBA.Green);
    }
}
