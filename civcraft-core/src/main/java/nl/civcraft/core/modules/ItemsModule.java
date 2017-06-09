package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nl.civcraft.core.gamecomponents.DropOnDestoyed;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.gamecomponents.Placable;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.rendering.ItemRenderer;

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

    }

    @Provides
    @Singleton
    @Named("apple")
    public PrefabManager apple(@Named("item") PrefabManager itemManager,
                               GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager appleManager = new PrefabManager(itemManager);
        appleManager.registerComponent(new ItemComponent.Factory("apple"));
        appleManager.registerComponent(itemRenderer);
        return appleManager;
    }

    @Provides
    @Singleton
    @Named("grassItem")
    public PrefabManager grassItem(@Named("item") PrefabManager itemManager,
                                   @Named("grass") PrefabManager grassManager,
                                   GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager grassItemPrefabManager = new PrefabManager(itemManager);
        grassItemPrefabManager.registerComponent(new ItemComponent.Factory("grassItem"));
        grassItemPrefabManager.registerComponent(itemRenderer);
        grassManager.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(grassItemPrefabManager, 1));
        grassItemPrefabManager.registerComponent(new Placable.Factory(grassManager, true));
        return grassItemPrefabManager;
    }

    @Provides
    @Singleton
    @Named("cobbleStoneItem")
    public PrefabManager cobbleStoneItem(@Named("item") PrefabManager itemManager,
                                         @Named("cobbleStone") PrefabManager cobbleStone,
                                         GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager cobbleStonePrefabManager = new PrefabManager(itemManager);
        cobbleStonePrefabManager.registerComponent(new ItemComponent.Factory("cobbleStone"));
        cobbleStonePrefabManager.registerComponent(itemRenderer);
        cobbleStone.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(cobbleStonePrefabManager, 1));
        cobbleStonePrefabManager.registerComponent(new Placable.Factory(cobbleStone, true));
        return cobbleStonePrefabManager;
    }

    @Provides
    @Singleton
    @Named("dirtItem")
    public PrefabManager dirtItem(@Named("item") PrefabManager itemManager,
                                  @Named("dirt") PrefabManager dirt,
                                  GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager dirtPrefabManager = new PrefabManager(itemManager);
        dirtPrefabManager.registerComponent(new ItemComponent.Factory("dirt"));
        dirtPrefabManager.registerComponent(itemRenderer);
        dirt.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(dirtPrefabManager, 1));
        dirtPrefabManager.registerComponent(new Placable.Factory(dirt, true));
        return dirtPrefabManager;
    }

    @Provides
    @Singleton
    @Named("treeTrunkItem")
    public PrefabManager treeTrunkItem(@Named("item") PrefabManager itemManager,
                                       @Named("treeTrunk") PrefabManager treeTrunk,
                                       GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager treeTrunkPrefabManager = new PrefabManager(itemManager);
        treeTrunkPrefabManager.registerComponent(new ItemComponent.Factory("treeTrunk"));
        treeTrunkPrefabManager.registerComponent(itemRenderer);
        treeTrunk.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(treeTrunkPrefabManager, 1));
        treeTrunkPrefabManager.registerComponent(new Placable.Factory(treeTrunk, true));
        return treeTrunkPrefabManager;
    }
}
