package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.DropOnDestoyed;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.rendering.ItemRenderer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class Items {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("apple")
    public PrefabManager apple(@Qualifier("item") PrefabManager itemManager,
                               ApplicationEventPublisher applicationEventPublisher,
                               GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager appleManager = new PrefabManager(applicationEventPublisher, itemManager);
        appleManager.registerComponent(new ItemComponent.Factory("apple"));
        appleManager.registerComponent(itemRenderer);
        return appleManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("grassItem")
    public PrefabManager grassItem(@Qualifier("item") PrefabManager itemManager,
                                   @Qualifier("grass") PrefabManager grassManager,
                                   ApplicationEventPublisher applicationEventPublisher,
                                   GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager grassItemPrefabManager = new PrefabManager(applicationEventPublisher, itemManager);
        grassItemPrefabManager.registerComponent(new ItemComponent.Factory("grassItem"));
        grassItemPrefabManager.registerComponent(itemRenderer);
        grassManager.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(grassItemPrefabManager, 1));
        return grassItemPrefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cobbleStoneItem")
    public PrefabManager cobbleStoneItem(@Qualifier("item") PrefabManager itemManager,
                                         @Qualifier("cobbleStone") PrefabManager cobbleStone,
                                         ApplicationEventPublisher applicationEventPublisher,
                                         GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager cobbleStonePrefabManager = new PrefabManager(applicationEventPublisher, itemManager);
        cobbleStonePrefabManager.registerComponent(new ItemComponent.Factory("cobbleStone"));
        cobbleStonePrefabManager.registerComponent(itemRenderer);
        cobbleStone.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(cobbleStonePrefabManager, 1));
        return cobbleStonePrefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("dirtItem")
    public PrefabManager dirtItem(@Qualifier("item") PrefabManager itemManager,
                                  @Qualifier("dirt") PrefabManager dirt,
                                  ApplicationEventPublisher applicationEventPublisher,
                                  GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager dirtPrefabManager = new PrefabManager(applicationEventPublisher, itemManager);
        dirtPrefabManager.registerComponent(new ItemComponent.Factory("dirt"));
        dirtPrefabManager.registerComponent(itemRenderer);
        dirt.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(dirtPrefabManager, 1));
        return dirtPrefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("treeTrunkItem")
    public PrefabManager treeTrunkItem(@Qualifier("item") PrefabManager itemManager,
                                       @Qualifier("treeTrunk") PrefabManager treeTrunk,
                                       ApplicationEventPublisher applicationEventPublisher,
                                       GameComponent.GameComponentFactory<ItemRenderer> itemRenderer) {
        PrefabManager treeTrunkPrefabManager = new PrefabManager(applicationEventPublisher, itemManager);
        treeTrunkPrefabManager.registerComponent(new ItemComponent.Factory("treeTrunk"));
        treeTrunkPrefabManager.registerComponent(itemRenderer);
        treeTrunk.getComponentFactory(DropOnDestoyed.Factory.class).
                ifPresent(dropOnDestoyedGameComponentFactory -> dropOnDestoyedGameComponentFactory.getDrops().put(treeTrunkPrefabManager, 1));
        return treeTrunkPrefabManager;
    }
}
