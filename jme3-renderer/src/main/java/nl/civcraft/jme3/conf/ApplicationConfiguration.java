package nl.civcraft.jme3.conf;


import nl.civcraft.jme3.Civcraft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Provides the civcraft application instance
 */
@Configuration
@EnableScheduling
public class ApplicationConfiguration {


    /**
     * @return Initialized civcraft instance
     */
    @Bean
    public Civcraft civcraft() {
        return Civcraft.getInstance();
    }


}
