package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import nl.civcraft.core.worldgeneration.VoxelGeometryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockConfiguration {

    @Autowired
    private AssetManager assetManager;

    @Bean
    public VoxelGeometryManager voxelGeometryManager()
    {
        return new VoxelGeometryManager();
    }

    @Bean
    public Geometry dirt()
    {
        Texture dirtTexture = assetManager.loadTexture(
                "textures/bdc_dirt03.png");
        Material dirtMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        dirtMaterial.setTexture("ColorMap", dirtTexture);
        Box box = new Box(0.5f, 0.5f, 0.5f);
        Geometry dirt = new Geometry("dirt", box);
        dirt.setMaterial(dirtMaterial);
        return dirt;

    }

    @Bean
    public Geometry cobbleStone()
    {
        Texture cobbleTexture = assetManager.loadTexture(
                "textures/bdc_cobblestone01.png");
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setTexture("ColorMap", cobbleTexture);
        Box box = new Box(0.5f, 0.5f, 0.5f);
        Geometry dirt = new Geometry("cobble", box);
        dirt.setMaterial(cobbleMaterial);
        return dirt;
    }
}
