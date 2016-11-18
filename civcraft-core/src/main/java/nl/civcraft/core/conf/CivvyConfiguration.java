package nl.civcraft.core.conf;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.gamecomponents.LimitedInventory;
import nl.civcraft.core.managers.GameObjectManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Wander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class CivvyConfiguration {

    @Autowired
    private AStarPathFinder pathFinder;


    @Bean
    public TaskManager taskManager(WorldManager worldManager) {
        TaskManager taskManager = new TaskManager(worldManager, pathFinder);
        taskManager.addTask(new Wander(pathFinder));
        return taskManager;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("civvy")
    public GameObjectManager civvyManager(ApplicationEventPublisher applicationEventPublisher, WorldManager worldManager) {
        GameObjectManager gameObjectManager = new GameObjectManager(applicationEventPublisher);
        gameObjectManager.registerComponent(new LimitedInventory.Factory(2));
        gameObjectManager.registerComponent(new Civvy.Factory());
        gameObjectManager.registerComponent(new GroundMovement.Factory(2f, worldManager));
        return gameObjectManager;
    }

}
