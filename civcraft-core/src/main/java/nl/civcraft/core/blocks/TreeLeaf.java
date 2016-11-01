package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import nl.civcraft.core.gamecomponents.AppleLeafVoxelRenderer;
import nl.civcraft.core.gamecomponents.HarvestFromInventory;
import nl.civcraft.core.gamecomponents.LimitedInventory;
import nl.civcraft.core.model.*;
import nl.civcraft.core.utils.BlockUtil;
import nl.civcraft.core.utils.MaterialUtil;
import nl.civcraft.core.utils.RandomUtil;
import nl.civcraft.core.worldgeneration.Apple;
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
public class TreeLeaf implements VoxelProducer {
    public static final String BLOCK_NAME = "treeLeaf";
    private final ApplicationEventPublisher publisher;
    private final Material oakMaterial;
    private final Material appleMaterial;

    @Autowired
    public TreeLeaf(AssetManager assetManager, ApplicationEventPublisher publisher) {
        oakMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/leaves_oak.png");
        appleMaterial = MaterialUtil.getUnshadedMaterial(assetManager, "textures/leaves_apple.png");
        this.publisher = publisher;
    }


    @Override
    public GameObject produce(int x, int y, int z) {
        Map<Face, VoxelFace> emptyBlock = BlockUtil.getQuadBlock(oakMaterial, oakMaterial, oakMaterial);
        Map<Face, VoxelFace> filledBlock = BlockUtil.getQuadBlock(appleMaterial, appleMaterial, appleMaterial);
        AppleLeafVoxelRenderer staticVoxelRenderer = new AppleLeafVoxelRenderer(emptyBlock, filledBlock);
        GameObject gameObject = new GameObject();
        Voxel voxel = new Voxel(x, y, z, BLOCK_NAME, publisher);
        gameObject.addComponent(voxel);
        gameObject.addComponent(staticVoxelRenderer);
        LimitedInventory limitedInventory = new LimitedInventory(4);
        int nextInt = RandomUtil.getNextInt(4);
        for (int i = 0; i < nextInt; i++) {
            limitedInventory.addItem(new Apple());
        }
        gameObject.addComponent(limitedInventory);
        gameObject.addComponent(new HarvestFromInventory());
        return gameObject;
    }

    @Override
    public String blockName() {
        return BLOCK_NAME;
    }

}
