package nl.civcraft.core.conf;

import nl.civcraft.core.Civcraft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {


    @Bean
    public Civcraft civcraft() {
        return Civcraft.instance;
    }


}
