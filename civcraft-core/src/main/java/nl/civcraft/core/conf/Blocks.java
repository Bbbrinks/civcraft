package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.VoxelRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class Blocks {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("grass")
    public PrefabManager grassManager(ApplicationEventPublisher applicationEventPublisher,
                                      @Qualifier("block") PrefabManager blockManager,
                                      VoxelManager voxelManager,
                                      VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, blockManager);
        Voxel.Factory voxel = new Voxel.Factory("grass", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cobbleStone")
    public PrefabManager cobbleStoneManager(ApplicationEventPublisher applicationEventPublisher,
                                            @Qualifier("block") PrefabManager blockManager,
                                            VoxelManager voxelManager,
                                            VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, blockManager);
        Voxel.Factory voxel = new Voxel.Factory("cobbleStone", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("dirt")
    public PrefabManager dirtManager(ApplicationEventPublisher applicationEventPublisher,
                                     @Qualifier("block") PrefabManager blockManager,
                                     VoxelManager voxelManager,
                                     VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, blockManager);
        Voxel.Factory voxel = new Voxel.Factory("dirt", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("treeLeaf")
    public PrefabManager treeLeafManager(ApplicationEventPublisher applicationEventPublisher,
                                         @Qualifier("block") PrefabManager blockManager,
                                         VoxelManager voxelManager,
                                         @Qualifier("apple") PrefabManager itemManager,
                                         VoxelRenderer.StateBasedVoxelRendererFactoryFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, blockManager);
        Function<GameObject, String> stateSupplier = (GameObject gameObject) -> gameObject.getComponent(InventoryComponent.class).map(i -> i.isEmpty() ? "empty" :
                "not-empty").orElse("empty");
        Voxel.Factory voxel = new Voxel.Factory("treeLeaf", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer.build(stateSupplier));
        prefabManager.registerComponent(new InventoryComponent.Factory(4));
        prefabManager.registerComponent(new HarvestFromInventory.Factory());
        prefabManager.registerComponent(new RandomItemGenerator.Factory(4, itemManager));
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("treeTrunk")
    public PrefabManager treeTrunkManager(ApplicationEventPublisher applicationEventPublisher,
                                          @Qualifier("block") PrefabManager blockManager,
                                          VoxelManager voxelManager,
                                          VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, blockManager);
        Voxel.Factory voxel = new Voxel.Factory("treeTrunk", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }
}
