package nl.civcraft.jme3.worldgeneration;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.events.ChunkModifiedEvent;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorldGeneratorState implements ActionListener {

    private static final String GENERATE_WORLD = "GENERATE_WORLD";
    private static final String OPTIMIZE_CHUNKS = "OPTIMIZE_CHUNKS";


    private final WorldGenerator worldGenerator;


    private final WorldManager worldManager;


    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public WorldGeneratorState(WorldGenerator worldGenerator, WorldManager worldManager, ApplicationEventPublisher applicationEventPublisher, InputManager inputManager) {
        this.worldGenerator = worldGenerator;
        this.worldManager = worldManager;
        this.applicationEventPublisher = applicationEventPublisher;
        registerInput(inputManager);
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
            List<Chunk> chunks = worldManager.getWorld().getChunks();
            for (Chunk chunk : chunks) {
                applicationEventPublisher.publishEvent(new ChunkModifiedEvent(chunk, this));
            }
        }
    }

    private void generateInitialChunk() {
        Thread thread = new Thread(worldGenerator, "World generation thread");
        thread.start();
    }


}
