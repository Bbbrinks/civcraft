package nl.civcraft.core.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import nl.civcraft.core.event.RequestClose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GlobalInput implements ActionListener {
    public static final String WIREFRAME = "WIREFRAME";
    private static final String EXIT = "EXIT";
    private final List<Material> materialList;
    private final InputManager inputManager;
    private final ApplicationEventPublisher publisher;
    private boolean wireframe;

    @Autowired
    public GlobalInput(List<Material> materialList, InputManager inputManager, ApplicationEventPublisher publisher) {
        this.materialList = materialList;
        this.inputManager = inputManager;
        this.publisher = publisher;
        registerInput(inputManager);
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
            publisher.publishEvent(new RequestClose(this));
        }
        if (name.equals(WIREFRAME)) {
            for (Material material : materialList) {
                material.getAdditionalRenderState().setWireframe(!wireframe);
            }
            wireframe = !wireframe;
        }
    }
}
