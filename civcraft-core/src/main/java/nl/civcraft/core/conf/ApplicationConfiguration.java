package nl.civcraft.core.conf;

import com.jme3.system.AppSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {


    @Bean
    public AppSettings appSettings() {
        AppSettings settings = new AppSettings(true);
        settings.setWidth(1024);
        settings.setHeight(768);
        settings.setTitle("CivCraft");
        return settings;
    }

}
