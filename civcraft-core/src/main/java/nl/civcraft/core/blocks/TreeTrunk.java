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
public class TreeTrunk extends SimpleBlock {
    public static final String BLOCK_NAME = "treeTrunk";
    private final Block block;

    @Autowired
    public TreeTrunk(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        block = block(assetManager);
    }

    private Block block(AssetManager assetManager) {
        Material treeTrunkTopMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/log_oak_top.png");
        Material treeTrunkSideMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/log_oak.png");
        return BlockUtil.getQuadBlock(BLOCK_NAME, treeTrunkTopMaterial, treeTrunkSideMaterial, treeTrunkTopMaterial);
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
