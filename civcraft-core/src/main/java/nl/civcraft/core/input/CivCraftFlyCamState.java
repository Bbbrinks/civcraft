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

    private final FlyingCamera flyCam;

    @Autowired
    public CivCraftFlyCamState(FlyingCamera camera) {
        this.flyCam = camera;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        flyCam.init(app.getCamera(), app.getInputManager());
        flyCam.registerWithInput();
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
