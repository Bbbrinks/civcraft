package nl.civcraft.core.conf;

import nl.civcraft.core.pathfinding.AStarPathFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathFinderConfiguration {

    @Bean
    public AStarPathFinder pathFinder() {
        return new AStarPathFinder();
    }
}
