package nl.civcraft.core.managers;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public BlockManager(List<VoxelProducer> producers) {
        this.producers = producers;
    }

    public Voxel createVoxel(String blockName, int x, int y, int z) {
        Optional<VoxelProducer> voxelProducerOptional = producers.stream().filter(p -> p.blockName().equals(blockName)).findFirst();
        if (!voxelProducerOptional.isPresent()) {
            throw new IllegalStateException("Could not find blcok of type " + blockName);
        }
        return voxelProducerOptional.get().produce(x, y, z);
    }
}
