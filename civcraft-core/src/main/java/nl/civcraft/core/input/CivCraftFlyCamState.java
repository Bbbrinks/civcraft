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

    private Application app;
    @Autowired
    private FlyingCamera flyCam;
    @Autowired
    private Spatial rootNode;
    @Autowired
    private AssetManager assetManager;

    public CivCraftFlyCamState() {
    }

    /**
     * This is called by SimpleApplication during initialize().
     */
    void setCamera(FlyingCamera cam) {
        this.flyCam = cam;
    }

    public FlyingCamera getCamera() {
        return flyCam;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        this.app = app;

        if (app.getInputManager() != null) {

            flyCam.init(app.getCamera());

            flyCam.registerWithInput(app.getInputManager());
        }

        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        final int SHADOWMAP_SIZE=1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        ViewPort viewPort = app.getViewPort();
        viewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
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

        if (app.getInputManager() != null) {
            flyCam.unregisterInput();
        }
    }
}
