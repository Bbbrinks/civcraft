package nl.civcraft.core.model;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface VoxelProducer {
    Voxel produce(int x, int y, int z);

    String blockName();
}
