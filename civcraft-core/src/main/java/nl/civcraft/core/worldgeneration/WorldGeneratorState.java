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

import java.util.List;

@Service
public class WorldGeneratorState extends AbstractAppState implements ActionListener {

    public static final String GENERATE_WORLD = "GENERATE_WORLD";
    private static final String OPTIMIZE_CHUNKS = "OPTIMIZE_CHUNKS";

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
        inputManager.addMapping(OPTIMIZE_CHUNKS, new KeyTrigger(KeyInput.KEY_O));
        inputManager.addListener(this, GENERATE_WORLD, OPTIMIZE_CHUNKS);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(GENERATE_WORLD)) {
            generateInitialChunk();
        }
        if (name.equals(OPTIMIZE_CHUNKS)) {
            List<Chunk> chunks = rootNode.descendantMatches(Chunk.class);
            for (Chunk chunk : chunks) {
                chunk.setOptimized(false);
            }
        }
    }

    public void generateInitialChunk() {
        rootNode.detachAllChildren();
        worldGenerator.generateHeightMap();
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                rootNode.attachChild(worldGenerator.generateChunk(x, z));
            }
        }
    }
}
