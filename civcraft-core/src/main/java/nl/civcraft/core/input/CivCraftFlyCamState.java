package nl.civcraft.core.input;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
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
        initialize();
    }

    public void initialize() {
        flyCam.registerWithInput();

        //TODO: move this light/shadow stuff somewhere else
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        rootNode.addLight(sun);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 2);
        dlsr.setLight(sun);
        mainViewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 2);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        mainViewPort.addProcessor(fpp);
    }


    public void cleanup() {
        flyCam.unregisterInput();
    }
}
