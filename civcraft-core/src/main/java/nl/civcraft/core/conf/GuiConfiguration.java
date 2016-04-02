package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GuiConfiguration {

    @Bean
    public NiftyJmeDisplay niftyJmeDisplay(AssetManager assetManager, InputManager inputManager, ViewPort guiViewPort) {
        return new NiftyJmeDisplay(assetManager, inputManager, null, guiViewPort);
    }

    @Bean
    public Node guiNode() {
        Node node = new Node("Gui Node");
        node.setQueueBucket(RenderQueue.Bucket.Gui);
        return node;
    }
}
