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
public class CobbleStone extends SimpleBlock {
    public static final String BLOCK_NAME = "cobble";
    private final Block block;

    @Autowired
    public CobbleStone(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        block = cobble(MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_cobblestone01.png"));
    }

    private Block cobble(Material cobbleMaterial) {
        return BlockUtil.getQuadBlock(BLOCK_NAME, cobbleMaterial, cobbleMaterial, cobbleMaterial);
    }

    @Override
    protected Block block() {
        return block;
    }

    @Override
    public String blockName() {
        return BLOCK_NAME;
    }
}
