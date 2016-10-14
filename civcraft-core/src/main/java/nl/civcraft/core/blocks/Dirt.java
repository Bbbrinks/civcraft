package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
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
public class Dirt extends SimpleBlock {
    public static final String BLOCK_NAME = "dirt";
    private final Block block;

    @Autowired
    public Dirt(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        block = dirt(MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_dirt03.png"));
    }

    private Block dirt(Material dirtMaterial) {
        return BlockUtil.getQuadBlock(BLOCK_NAME, dirtMaterial, dirtMaterial, dirtMaterial);
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
