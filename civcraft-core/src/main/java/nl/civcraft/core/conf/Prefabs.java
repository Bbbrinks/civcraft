package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TickManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
    public PrefabManager civvyManager(
            VoxelManager voxelManager,
            AStarPathFinder aStarPathFinder,
            TickManager tickManager) {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new InventoryComponent.Factory(2));
        prefabManager.registerComponent(new Civvy.Factory());
        prefabManager.registerComponent(new GroundMovement.Factory(2f, voxelManager, aStarPathFinder));
        prefabManager.registerComponent(new Hunger.Factory(tickManager));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("item")
    public PrefabManager itemManager() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new Haulable.Factory());
        return prefabManager;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("block")
    public PrefabManager blockManager(
            Neighbour.Factory neighbourComponent) {
        return new PrefabManager();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("stockpile")
    public PrefabManager stockpile(
            Stockpile.Factory stockpileComponent) {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(stockpileComponent);
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("voxelHighlight")
    public PrefabManager voxelHighlight() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new VoxelHighlight.Factory());
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("planningGhost")
    public PrefabManager planningGhost() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new PlanningGhost.Factory());
        return prefabManager;
    }
}
