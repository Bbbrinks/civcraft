package nl.civcraft.core.worldgeneration;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorldGeneratorState extends AbstractAppState implements ActionListener {

    public static final String GENERATE_WORLD = "GENERATE_WORLD";

    @Autowired
    private AssetManager assetManager;

    @Autowired
    private WorldGenerator worldGenerator;

    @Autowired
    private Node rootNode;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);

        registerInput(app.getInputManager());
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(GENERATE_WORLD, new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, GENERATE_WORLD);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(GENERATE_WORLD)) {
            generateInitialChunk();
        }
    }

    public void generateInitialChunk() {
        rootNode.detachAllChildren();
        rootNode.attachChild(worldGenerator.generateChunk(0,0,0));
    }


}
