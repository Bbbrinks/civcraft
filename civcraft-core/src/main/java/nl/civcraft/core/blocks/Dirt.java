package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.VoxelFace;
import nl.civcraft.core.utils.BlockUtil;
import nl.civcraft.core.utils.MaterialUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class Dirt extends SimpleBlock {
    public static final String BLOCK_NAME = "dirt";
    private final Material dirtMaterial;

    @Autowired
    public Dirt(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        dirtMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_dirt03.png");
    }

    @Override
    public String blockName() {
        return BLOCK_NAME;
    }


    @Override
    protected Map<Face, VoxelFace> block() {
        return BlockUtil.getQuadBlock(dirtMaterial, dirtMaterial, dirtMaterial);
    }
}
