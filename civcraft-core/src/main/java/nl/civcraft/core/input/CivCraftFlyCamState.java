package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
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

/**
 * Created by Bob on 20-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftFlyCamState extends AbstractAppState {

    private final FlyingCamera flyCam;
    @Autowired
    private Spatial rootNode;
    @Autowired
    private AssetManager assetManager;

    @Autowired
    public CivCraftFlyCamState(FlyingCamera camera) {
        this.flyCam = camera;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        flyCam.init(app.getCamera(), app.getInputManager());
        flyCam.registerWithInput();

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
        rootNode.addLight(sun);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 1);
        dlsr.setLight(sun);
        ViewPort viewPort = app.getViewPort();
        viewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 1);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        flyCam.setEnabled(enabled);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        flyCam.unregisterInput();
    }
}
