package nl.civcraft.core.conf;

import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Wander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
