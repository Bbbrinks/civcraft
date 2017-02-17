package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
@Configuration
public class Prefabs {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("civvy")
    public PrefabManager civvyManager(ApplicationEventPublisher applicationEventPublisher, VoxelManager voxelManager, AStarPathFinder aStarPathFinder) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(new InventoryComponent.Factory(2));
        prefabManager.registerComponent(new Civvy.Factory());
        prefabManager.registerComponent(new GroundMovement.Factory(2f, voxelManager, aStarPathFinder));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("item")
    public PrefabManager itemManager(ApplicationEventPublisher applicationEventPublisher) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(new Haulable.Factory());
        return prefabManager;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("block")
    public PrefabManager blockManager(ApplicationEventPublisher applicationEventPublisher, Neighbour.Factory
            neighbourComponent) {
        return new PrefabManager(applicationEventPublisher, null);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("stockpile")
    public PrefabManager stockpile(ApplicationEventPublisher applicationEventPublisher, Stockpile.Factory
            stockpileComponent) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(stockpileComponent);
        return prefabManager;
    }
}
