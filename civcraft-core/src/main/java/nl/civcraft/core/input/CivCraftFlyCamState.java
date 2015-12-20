package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
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
