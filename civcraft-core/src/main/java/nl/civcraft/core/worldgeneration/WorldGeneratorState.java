package nl.civcraft.core.worldgeneration;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.events.ChunkModifiedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorldGeneratorState extends AbstractAppState implements ActionListener {

    private static final String GENERATE_WORLD = "GENERATE_WORLD";
    private static final String OPTIMIZE_CHUNKS = "OPTIMIZE_CHUNKS";


    private final WorldGenerator worldGenerator;


    private final WorldManager worldManager;


    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public WorldGeneratorState(WorldGenerator worldGenerator, WorldManager worldManager, ApplicationEventPublisher applicationEventPublisher) {
        this.worldGenerator = worldGenerator;
        this.worldManager = worldManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

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
