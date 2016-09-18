package nl.civcraft.core.conf;

import nl.civcraft.core.Civcraft;
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
