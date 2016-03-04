package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalInput extends AbstractAppState implements ActionListener {
    public static final String WIREFRAME = "WIREFRAME";
    private static final String EXIT = "EXIT";
    private Application app;

    @Autowired
    private List<Material> materialList;
    private boolean wireframe;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        registerInput(app.getInputManager());
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(EXIT, new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addListener(this, EXIT);

        inputManager.addMapping(WIREFRAME, new KeyTrigger(KeyInput.KEY_F11));
        inputManager.addListener(this, WIREFRAME);

    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(EXIT)) {
            app.stop();
        }
        if (name.equals(WIREFRAME)) {
            for (Material material : materialList) {
                material.getAdditionalRenderState().setWireframe(!wireframe);
            }
            wireframe = !wireframe;
        }
    }
}
