package nl.civcraft.core.conf;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;
import nl.civcraft.core.CivCraftApplication;
import nl.civcraft.core.worldgeneration.WorldGeneratorState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackageClasses = WorldGeneratorState.class)
public class InitialConfigruation {


    @Bean
    @Scope("singleton")
    public Application mainApplication(WorldGeneratorState worldGeneratorState) {
        //TODO: Replace SimpleApplication with own implementation
        CivCraftApplication application = new CivCraftApplication();
        application.addAppStates(new FlyCamAppState(), new DebugKeysAppState(), worldGeneratorState);
        return application;
    }

    @Bean
    public Node rootNode() {
        return new Node("Root Node");
    }

    @Bean
    public Node guiNode() {
        return new Node("Gui Node");
    }

    @Bean
    @Scope("singleton")
    public AssetManager assetManager() {
        return JmeSystem.newAssetManager(
                Thread.currentThread().getContextClassLoader()
                        .getResource("com/jme3/asset/Desktop.cfg"));
    }
}
