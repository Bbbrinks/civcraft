package nl.civcraft.core.conf;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.state.AppState;
import com.jme3.scene.Node;
import nl.civcraft.core.input.CivCraftFlyCamState;
import nl.civcraft.core.input.FlyingCamera;
import nl.civcraft.core.input.GlobalInput;
import nl.civcraft.core.input.VoxelSelectionInput;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.WorldManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InputConfiguration {

    @Bean
    public AppState globalInput() {
        return new GlobalInput();
    }

    @Bean
    public AppState flyCamAppState(FlyingCamera flyingCamera) {
        return new CivCraftFlyCamState(flyingCamera);
    }

    @Bean
    public FlyingCamera flyingCamera(){
        return new FlyingCamera();
    }

    @Bean
    public AppState debugKeysAppState() {
        return new DebugKeysAppState();
    }

    @Bean
    public AppState voxelSelectionInput(){
        return new VoxelSelectionInput();
    }

    @Bean
    public WorldManager worldManager(Node rootNode, TaskManager taskManager)
    {
        return new WorldManager(rootNode, taskManager);
    }
}
