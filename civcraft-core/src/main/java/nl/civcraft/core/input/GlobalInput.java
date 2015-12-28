package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import org.springframework.stereotype.Service;

@Service
public class GlobalInput extends AbstractAppState implements ActionListener {
    private static final String EXIT = "EXIT";
    private Application app;



    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        registerInput(app.getInputManager());
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, EXIT);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(EXIT)) {
            app.stop();
        }
    }
}
