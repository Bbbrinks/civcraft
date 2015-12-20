package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.managers.BlockOptimizer;
import nl.civcraft.core.managers.SingleGeometryBoxBlockOptimizer;
import nl.civcraft.core.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockConfiguration {

    @Autowired
    private AssetManager assetManager;

    @Bean
    public BlockManager voxelGeometryManager()
    {
        return new BlockManager();
    }

    @Bean
    public Block dirt(BlockOptimizer blockOptimizer)
    {
        Texture dirtTexture = assetManager.loadTexture(
                "textures/bdc_dirt03.png");
        Material dirtMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        dirtMaterial.setTexture("ColorMap", dirtTexture);
        return createSingleGeometryBoxBlock(dirtMaterial, "dirt", blockOptimizer);
    }

    @Bean
    public Block cobbleStone(BlockOptimizer blockOptimizer)
    {
        Texture cobbleTexture = assetManager.loadTexture(
                "textures/bdc_cobblestone01.png");
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setTexture("ColorMap", cobbleTexture);
        return createSingleGeometryBoxBlock(cobbleMaterial, "cobble", blockOptimizer);
    }

    private Block createSingleGeometryBoxBlock(Material cobbleMaterial, String name, BlockOptimizer blockOptimizer) {
        Box box = new Box(0.5f, 0.5f, 0.5f);
        Geometry geometry = new Geometry("box", box);
        geometry.setMaterial(cobbleMaterial);
        Block block = new Block(name, blockOptimizer);
        block.attachChild(geometry);
        return block;
    }

    @Bean
    public BlockOptimizer singleGeometryBoxBlockOptimizer()
    {
        return new SingleGeometryBoxBlockOptimizer();
    }
}
