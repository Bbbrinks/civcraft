package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
public class Grass extends SimpleBlock {
    public static final String BLOCK_NAME = "grass";
    private final Material grassTopMaterial;
    private final Material grassSideMaterial;
    private final Material grassBottomMaterial;

    @Autowired
    public Grass(AssetManager assetManager, ApplicationEventPublisher publisher) {
        super(publisher);
        grassTopMaterial = MaterialUtil.getGrayScaleMaterial(assetManager, new ColorRGBA(0.51f, 0.83f, 0.24f, 1.0f), "textures/grass_top.png");

        grassSideMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_grass_side01.png");
        grassBottomMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/bdc_dirt03.png");

    }

    @Override
    public String blockName() {
        return BLOCK_NAME;
    }

    @Override
    protected Map<Face, VoxelFace> block() {
        return BlockUtil.getQuadBlock(grassTopMaterial, grassSideMaterial, grassBottomMaterial);
    }
}
