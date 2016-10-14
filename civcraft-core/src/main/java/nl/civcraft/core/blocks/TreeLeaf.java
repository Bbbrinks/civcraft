package nl.civcraft.core.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import nl.civcraft.core.gamecomponents.AppleLeafVoxelRenderer;
import nl.civcraft.core.gamecomponents.HarvestFromInventory;
import nl.civcraft.core.gamecomponents.LimitedInventory;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import nl.civcraft.core.utils.BlockUtil;
import nl.civcraft.core.utils.MaterialUtil;
import nl.civcraft.core.utils.RandomUtil;
import nl.civcraft.core.worldgeneration.Apple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class TreeLeaf implements VoxelProducer {
    public static final String BLOCK_NAME = "treeLeaf";
    private final Block emptyBlock;
    private final Block filledBlock;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public TreeLeaf(AssetManager assetManager, ApplicationEventPublisher publisher) {
        emptyBlock = treeLeaf(assetManager);
        filledBlock = appleTreeLeaf(assetManager);
        this.publisher = publisher;
    }

    private Block treeLeaf(AssetManager assetManager) {
        Material material = MaterialUtil.getUnshadedMaterial(assetManager, "textures/leaves_oak.png");
        return BlockUtil.getQuadBlock("treeLeaf", material, material, material);
    }

    private Block appleTreeLeaf(AssetManager assetManager) {
        Material material = MaterialUtil.getUnshadedMaterial(assetManager, "textures/leaves_apple.png");
        return BlockUtil.getQuadBlock("appleLeaf", material, material, material);
    }

    @Override
    public Voxel produce(int x, int y, int z) {
        AppleLeafVoxelRenderer staticVoxelRenderer = new AppleLeafVoxelRenderer(emptyBlock, filledBlock);
        Voxel voxel = new Voxel(x, y, z, BLOCK_NAME, publisher);
        voxel.addComponent(staticVoxelRenderer);
        LimitedInventory limitedInventory = new LimitedInventory(4);
        int nextInt = RandomUtil.getNextInt(4);
        for (int i = 0; i < nextInt; i++) {
            limitedInventory.addItem(new Apple());
        }
        voxel.addComponent(limitedInventory);
        voxel.addComponent(new HarvestFromInventory());
        return voxel;
    }

    @Override
    public String blockName() {
        return BLOCK_NAME;
    }

}
