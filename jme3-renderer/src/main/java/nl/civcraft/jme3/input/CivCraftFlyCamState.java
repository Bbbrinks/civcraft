package nl.civcraft.jme3.input;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CivCraftFlyCamState {

    private final FlyingCamera flyCam;
    private final Spatial rootNode;
    private final AssetManager assetManager;
    private final ViewPort mainViewPort;

    @Autowired
    public CivCraftFlyCamState(FlyingCamera camera, Spatial rootNode, AssetManager assetManager, ViewPort mainViewPort) {
        this.flyCam = camera;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.mainViewPort = mainViewPort;
        flyCam.registerWithInput();
    }

    public void cleanup() {
        flyCam.unregisterInput();
    }
}
