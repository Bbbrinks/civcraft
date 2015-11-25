package nl.civcraft.core.conf;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.JmeSystem;
import nl.civcraft.core.CivCraftApplication;
import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.input.GlobalInput;
import nl.civcraft.core.worldgeneration.WorldGeneratorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = WorldGeneratorState.class)
public class InitialConfigruation {

    @Autowired
    List<AppState> appStateList;

    @Bean
    @Scope("singleton")
    public Application mainApplication() {
        //TODO: Replace SimpleApplication with own implementation
        CivCraftApplication application = new CivCraftApplication();
        application.addAppStates(appStateList);
        return application;
    }

    @Bean
    public AppState worldGeneratorState(){
        return new WorldGeneratorState();
    }
    @Bean
    public AppState debugStatsState(){
        return new DebugStatsState();
    }

    @Bean
    public AppState globalInput(){
        return new GlobalInput();
    }

    @Bean
    public AppState flyCamAppState(){
        return new FlyCamAppState();
    }

    @Bean
    public AppState debugKeysAppState(){
        return new DebugKeysAppState();
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
