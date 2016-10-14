package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.utils.BlockUtil;
import nl.civcraft.core.utils.MaterialUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class Grass extends SimpleBlock {
    public static final String BLOCK_NAME = "grass";
    private final Block block;

    @Autowired
    public Grass(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        block = grass(assetManager);

    }

    private Block grass(AssetManager assetManager) {
        Material grassTopMaterial = MaterialUtil.getGrayScaleMaterial(assetManager, new ColorRGBA(0.51f, 0.83f, 0.24f, 1.0f), "textures/grass_top.png");

        Material grassSideMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_grass_side01.png");
        Material grassBottomMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_dirt03.png");
        return BlockUtil.getQuadBlock(BLOCK_NAME, grassTopMaterial, grassSideMaterial, grassBottomMaterial);
    }


    @Override
    public String blockName() {
        return BLOCK_NAME;
    }

    @Override
    protected Block block() {
        return block;
    }
}
