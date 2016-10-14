package nl.civcraft.core.blocks;

import nl.civcraft.core.gamecomponents.StaticVoxelRenderer;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class SimpleBlock implements VoxelProducer {
    private final ApplicationEventPublisher publisher;

    protected SimpleBlock(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public Voxel produce(int x, int y, int z) {
        StaticVoxelRenderer staticVoxelRenderer = new StaticVoxelRenderer(block());
        Voxel voxel = new Voxel(x, y, z, blockName(), publisher);
        voxel.addComponent(staticVoxelRenderer);
        return voxel;
    }

    protected abstract Block block();

}
