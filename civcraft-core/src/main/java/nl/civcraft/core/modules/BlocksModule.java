package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.VoxelRenderer;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BlocksModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    @Named("grass")
    public PrefabManager grassManager(@Named("block") PrefabManager blockManager,
                                      VoxelManager voxelManager,
                                      VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("grass", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("cobbleStone")
    public PrefabManager cobbleStoneManager(@Named("block") PrefabManager blockManager,
                                            VoxelManager voxelManager,
                                            VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("cobbleStone", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("dirt")
    public PrefabManager dirtManager(@Named("block") PrefabManager blockManager,
                                     VoxelManager voxelManager,
                                     VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("dirt", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("treeLeaf")
    public PrefabManager treeLeafManager(
            @Named("block") PrefabManager blockManager,
            VoxelManager voxelManager,
            @Named("apple") PrefabManager itemManager,
            VoxelRenderer.StateBasedVoxelRendererFactoryFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
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

    @Provides
    @Singleton
    @Named("treeTrunk")
    public PrefabManager treeTrunkManager(
            @Named("block") PrefabManager blockManager,
            VoxelManager voxelManager,
            VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("treeTrunk", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(voxelRenderer);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }
}
