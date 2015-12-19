package nl.civcraft.core.conf;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.control.LodControl;
import com.jme3.system.JmeSystem;
import nl.civcraft.core.CivCraftApplication;
import nl.civcraft.core.worldgeneration.ChunkBuilder;
import nl.civcraft.core.worldgeneration.WorldGeneratorState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = {WorldGeneratorState.class, WorldGeneration.class, BlockConfiguration.class})
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
    public ChunkBuilder chunkBuilder() {
        return new ChunkBuilder();
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
    public LodControl lodControl()
    {
        return new LodControl();
    }

    @Bean
    @Scope("singleton")
    public AssetManager assetManager() {
        return JmeSystem.newAssetManager(
                Thread.currentThread().getContextClassLoader()
                        .getResource("com/jme3/asset/Desktop.cfg"));
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
