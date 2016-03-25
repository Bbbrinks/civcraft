package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.AppleLeafVoxelRenderer;
import nl.civcraft.core.gamecomponents.HarvestFromInventory;
import nl.civcraft.core.gamecomponents.LimitedInventory;
import nl.civcraft.core.gamecomponents.StaticVoxelRenderer;
import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import nl.civcraft.core.utils.RandomUtil;
import nl.civcraft.core.worldgeneration.Apple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VoxelConfiguration {

    @Autowired
    private BlockManager blockManager;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Bean
    public VoxelProducer dirtVoxelProducer() {
        return (x, y, z) -> {
            Block block = blockManager.findBlock("dirt");
            StaticVoxelRenderer staticVoxelRenderer = new StaticVoxelRenderer(block);
            Voxel voxel = new Voxel(x, y, z, "dirt", publisher);
            voxel.addComponent(staticVoxelRenderer);
            return voxel;
        };
    }

    @Bean
    public VoxelProducer cobbleVoxelProducer() {
        return (x, y, z) -> {
            Block block = blockManager.findBlock("cobble");
            StaticVoxelRenderer staticVoxelRenderer = new StaticVoxelRenderer(block);
            Voxel voxel = new Voxel(x, y, z, "cobble", publisher);
            voxel.addComponent(staticVoxelRenderer);
            return voxel;
        };
    }

    @Bean
    public VoxelProducer grassVoxelProducer() {
        return (x, y, z) -> {
            Block block = blockManager.findBlock("grass");
            StaticVoxelRenderer staticVoxelRenderer = new StaticVoxelRenderer(block);
            Voxel voxel = new Voxel(x, y, z, "grass", publisher);
            voxel.addComponent(staticVoxelRenderer);
            return voxel;
        };
    }

    @Bean
    public VoxelProducer treeTrunkVoxelProducer() {
        return (x, y, z) -> {
            Block block = blockManager.findBlock("treeTrunk");
            StaticVoxelRenderer staticVoxelRenderer = new StaticVoxelRenderer(block);
            Voxel voxel = new Voxel(x, y, z, "treeTrunk", publisher);
            voxel.addComponent(staticVoxelRenderer);
            return voxel;
        };
    }

    @Bean
    public VoxelProducer treeLeafVoxelProducer() {
        return (x, y, z) -> {
            Block treeLeaf = blockManager.findBlock("treeLeaf");
            Block appleTreeLeaf = blockManager.findBlock("appleLeaf");
            AppleLeafVoxelRenderer staticVoxelRenderer = new AppleLeafVoxelRenderer(treeLeaf, appleTreeLeaf);
            Voxel voxel = new Voxel(x, y, z, "treeLeaf", publisher);
            voxel.addComponent(staticVoxelRenderer);
            LimitedInventory limitedInventory = new LimitedInventory(4);
            int nextInt = RandomUtil.getNextInt(4);
            for (int i = 0; i < nextInt; i++) {
                limitedInventory.addItem(new Apple());
            }
            voxel.addComponent(limitedInventory);
            voxel.addComponent(new HarvestFromInventory());
            return voxel;
        };
    }

}
