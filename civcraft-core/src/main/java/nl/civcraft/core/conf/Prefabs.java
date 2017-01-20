package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.npc.Civvy;
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
    public PrefabManager civvyManager(ApplicationEventPublisher applicationEventPublisher, VoxelManager voxelManager) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(new LimitedInventory.Factory(2));
        prefabManager.registerComponent(new Civvy.Factory());
        prefabManager.registerComponent(new GroundMovement.Factory(2f, voxelManager));
        return prefabManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("item")
    public PrefabManager itemManager(ApplicationEventPublisher applicationEventPublisher) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(new ItemComponent.Factory());
        prefabManager.registerComponent(new Haulable.Factory());
        return prefabManager;
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("block")
    public PrefabManager blockManager(ApplicationEventPublisher applicationEventPublisher, Neighbour.Factory
            neighbourComponent) {
        PrefabManager prefabManager = new PrefabManager(applicationEventPublisher, null);
        prefabManager.registerComponent(neighbourComponent);
        return prefabManager;
    }
}