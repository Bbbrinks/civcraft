package nl.civcraft.jme3.worldgeneration;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class WorldGeneratorState implements ActionListener {

    private static final String GENERATE_WORLD = "GENERATE_WORLD";
    private static final String OPTIMIZE_CHUNKS = "OPTIMIZE_CHUNKS";


    private final WorldGenerator worldGenerator;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final VoxelManager voxelManager;

    @Autowired
    public WorldGeneratorState(WorldGenerator worldGenerator, ApplicationEventPublisher applicationEventPublisher, InputManager inputManager, VoxelManager voxelManager) {
        this.worldGenerator = worldGenerator;
        this.applicationEventPublisher = applicationEventPublisher;
        this.voxelManager = voxelManager;
        registerInput(inputManager);
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(GENERATE_WORLD, new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, GENERATE_WORLD, OPTIMIZE_CHUNKS);
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(GENERATE_WORLD)) {
            generateInitialChunk();
        }

    }

    private void generateInitialChunk() {
        Thread thread = new Thread(worldGenerator, "World generation thread");
        thread.start();
    }


}
