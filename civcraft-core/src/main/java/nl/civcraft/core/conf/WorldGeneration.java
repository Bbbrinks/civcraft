package nl.civcraft.core.conf;

import com.jme3.app.state.AppState;
import nl.civcraft.core.worldgeneration.HillsGenerator;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import nl.civcraft.core.worldgeneration.WorldGeneratorState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:world-generation.properties")
public class WorldGeneration {

    @Value("${max_hill_height}")
    private float maxHillHeight;
    @Value("${min_hill_radius}")
    private float minHillRadius;
    @Value("${max_hill_radius}")
    private float maxHillRadius;
    @Value("${min_hill_count}")
    private int minHills;
    @Value("${max_hill_count}")
    private int maxHills;
    @Value("${height_map_width}")
    private int heightMapWidth;
    @Value("${height_map_height}")
    private int heightMapHeight;

    @Bean
    public HillsGenerator rollingHillsGenerator() {
        return new HillsGenerator(maxHillHeight, minHillRadius, maxHillRadius, minHills, maxHills);
    }


    @Bean
    public AppState worldGeneratorState() {
        return new WorldGeneratorState();
    }

    @Bean
    public WorldGenerator worldGenerator() {
        return new WorldGenerator(heightMapWidth, heightMapHeight);
    }

}
