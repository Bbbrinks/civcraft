package nl.civcraft.core.conf;

import com.jme3.app.Application;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import nl.civcraft.core.CivCraftApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @Scope("singleton")
    public Application mainApplication(AppSettings settings, Node rootNode, Node guiNode) {
        CivCraftApplication application = new CivCraftApplication(rootNode, guiNode);
        application.setSettings(settings);
        return application;
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
