package nl.civcraft.core.conf;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import nl.civcraft.core.CivCraftApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @Scope("singleton")
    public Application mainApplication(JmeContext context) {
        CivCraftApplication application = new CivCraftApplication();
        application.setContext(context);

        return application;
    }

    @Bean
    public JmeContext context(AppSettings settings) {
        JmeContext context = JmeSystem.newContext(settings, JmeContext.Type.Display);
        return context;
    }

    @Bean
    public AppSettings appSettings() {
        AppSettings settings = new AppSettings(true);
        settings.setWidth(1024);
        settings.setHeight(768);
        settings.setTitle("CivCraft");
        return settings;
    }

}
