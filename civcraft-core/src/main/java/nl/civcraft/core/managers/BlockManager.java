package nl.civcraft.core.managers;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class BlockManager {
    private final List<VoxelProducer> producers;
    private final ApplicationEventPublisher eventPublisher;
    private final WorldManager worldManager;

    @Autowired
    public BlockManager(List<VoxelProducer> producers, ApplicationEventPublisher eventPublisher, WorldManager worldManager) {
        this.producers = producers;
        this.eventPublisher = eventPublisher;
        this.worldManager = worldManager;
    }

    public void createVoxel(String blockName, int x, int y, int z) {
        Optional<VoxelProducer> voxelProducerOptional = producers.stream().filter(p -> p.blockName().equals(blockName)).findFirst();
        if (!voxelProducerOptional.isPresent()) {
            throw new IllegalStateException("Could not find block of type " + blockName);
        }
        GameObject gameObject = voxelProducerOptional.get().produce(x, y, z);
        //TODO: Move voxel management out of World
        worldManager.getWorld().addVoxel(gameObject.getComponent(Voxel.class).get());
        eventPublisher.publishEvent(new GameObjectCreatedEvent(gameObject));
    }
}
