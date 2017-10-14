package nl.civcraft.core.modules;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nl.civcraft.core.gamecomponents.DropOnDestoyed;
import nl.civcraft.core.gamecomponents.HarvestFromInventory;
import nl.civcraft.core.gamecomponents.InventoryComponent;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.PrefabManagerManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class BlocksModule extends AbstractModule {
    @Override
    protected void configure() {
        // Nothing yet
    }

    @Provides
    @Singleton
    @Named("grass")
    public PrefabManager grassManager(@Named("block") PrefabManager blockManager,
                                      VoxelManager voxelManager,
                                      PrefabManagerManager prefabManagerManager) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("grass", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(ImmutableMap.<Supplier<PrefabManager>, Integer>builder().put(() -> prefabManagerManager.get("grassItem"), 1).build()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("cobbleStone")
    public PrefabManager cobbleStoneManager(@Named("block") PrefabManager blockManager,
                                            VoxelManager voxelManager,
                                            PrefabManagerManager prefabManagerManager) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("cobbleStone", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(ImmutableMap.<Supplier<PrefabManager>, Integer>builder().put(() -> prefabManagerManager.get("cobbleStoneItem"), 1).build()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("dirt")
    public PrefabManager dirtManager(@Named("block") PrefabManager blockManager,
                                     VoxelManager voxelManager,
                                     PrefabManagerManager prefabManagerManager) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("dirt", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(ImmutableMap.<Supplier<PrefabManager>, Integer>builder().put(() -> prefabManagerManager.get("dirtItem"), 1).build()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("treeLeaf")
    public PrefabManager treeLeafManager(
            @Named("block") PrefabManager blockManager,
            VoxelManager voxelManager,
            @Named("apple") PrefabManager itemManager,
            PrefabManagerManager prefabManagerManager) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Function<GameObject, String> stateSupplier = (GameObject gameObject) -> gameObject.getComponent(InventoryComponent.class).map(i -> i.isEmpty() ? "empty" :
                "not-empty").orElse("empty");
        Voxel.Factory voxel = new Voxel.Factory("treeLeaf", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(new InventoryComponent.Factory(4));
        prefabManager.registerComponent(new HarvestFromInventory.Factory());
        /*prefabManager.registerComponent(new RandomItemGenerator.Factory(4, itemManager));*/
        prefabManager.registerComponent(new DropOnDestoyed.Factory(new HashMap<>()));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("treeTrunk")
    public PrefabManager treeTrunkManager(
            @Named("block") PrefabManager blockManager,
            VoxelManager voxelManager,
            PrefabManagerManager prefabManagerManager) {
        PrefabManager prefabManager = new PrefabManager(blockManager);
        Voxel.Factory voxel = new Voxel.Factory("treeTrunk", voxelManager);
        prefabManager.registerComponent(voxel);
        prefabManager.registerComponent(new DropOnDestoyed.Factory(ImmutableMap.<Supplier<PrefabManager>, Integer>builder().put(() -> prefabManagerManager.get("treeTrunkItem"), 1).build()));
        return prefabManager;
    }
}
