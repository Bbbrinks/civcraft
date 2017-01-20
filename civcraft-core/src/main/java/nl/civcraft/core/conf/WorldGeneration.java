package nl.civcraft.core.conf;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.worldgeneration.HillsGenerator;
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

    @Bean
    public HillsGenerator rollingHillsGenerator() {
        return new HillsGenerator(maxHillHeight, minHillRadius, maxHillRadius, minHills, maxHills);
    }

    @Bean
    public VoxelManager voxelManager() {
        return new VoxelManager();
    }

}
