package nl.civcraft.core.conf;

import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Wander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CivvyConfiguration {

    @Autowired
    private AStarPathFinder pathFinder;


    //TODO move this to a AutoWander component or something
    @Bean
    public TaskManager taskManager(VoxelManager voxelManager) {
        TaskManager taskManager = new TaskManager(pathFinder, voxelManager);
        taskManager.addTask(new Wander(pathFinder));
        return taskManager;
    }

}
