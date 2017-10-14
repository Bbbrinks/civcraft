package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.gamecomponents.Placable;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.PrefabManagerManager;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class ItemsModule extends AbstractModule {
    @Override
    protected void configure() {
        // Nothing yet
    }

    @Provides
    @Singleton
    @Named("apple")
    public PrefabManager apple(@Named("item") PrefabManager itemManager,
                               PrefabManagerManager prefabManagerManager) {
        PrefabManager appleManager = new PrefabManager(itemManager);
        appleManager.registerComponent(new ItemComponent.Factory("apple"));
        return appleManager;
    }

    @Provides
    @Singleton
    @Named("grassItem")
    public PrefabManager grassItem(@Named("item") PrefabManager itemManager,
                                   PrefabManagerManager prefabManagerManager) {
        PrefabManager grassItemPrefabManager = new PrefabManager(itemManager);
        grassItemPrefabManager.registerComponent(new ItemComponent.Factory("grassItem"));
        grassItemPrefabManager.registerComponent(new Placable.Factory(() -> prefabManagerManager.get("grass"), true));
        return grassItemPrefabManager;
    }

    @Provides
    @Singleton
    @Named("cobbleStoneItem")
    public PrefabManager cobbleStoneItem(@Named("item") PrefabManager itemManager,
                                         PrefabManagerManager prefabManagerManager) {
        PrefabManager cobbleStonePrefabManager = new PrefabManager(itemManager);
        cobbleStonePrefabManager.registerComponent(new ItemComponent.Factory("cobbleStone"));
        cobbleStonePrefabManager.registerComponent(new Placable.Factory(() -> prefabManagerManager.get("cobbleStone"), true));
        return cobbleStonePrefabManager;
    }

    @Provides
    @Singleton
    @Named("dirtItem")
    public PrefabManager dirtItem(@Named("item") PrefabManager itemManager,
                                  PrefabManagerManager prefabManagerManager) {
        PrefabManager dirtPrefabManager = new PrefabManager(itemManager);
        dirtPrefabManager.registerComponent(new ItemComponent.Factory("dirt"));
        dirtPrefabManager.registerComponent(new Placable.Factory(() -> prefabManagerManager.get("dirt"), true));
        return dirtPrefabManager;
    }

    @Provides
    @Singleton
    @Named("treeTrunkItem")
    public PrefabManager treeTrunkItem(@Named("item") PrefabManager itemManager,
                                       PrefabManagerManager prefabManagerManager) {
        PrefabManager treeTrunkPrefabManager = new PrefabManager(itemManager);
        treeTrunkPrefabManager.registerComponent(new ItemComponent.Factory("treeTrunk"));
        treeTrunkPrefabManager.registerComponent(new Placable.Factory(() -> prefabManagerManager.get("treeTrunk"), true));
        return treeTrunkPrefabManager;
    }
}
