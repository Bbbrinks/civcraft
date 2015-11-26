package nl.civcraft.core.worldgeneration;

import com.jme3.asset.AssetManager;
import com.jme3.scene.control.LodControl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkBuilder {
    @Autowired
    private AssetManager assetManager;

    @Autowired
    private LodControl lodControl;

    private static final int chunkSize = 10;

    public Chunk buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        Chunk chunk = new Chunk(lodControl);
        int chunkMinX = chunkX * chunkSize;
        int chunkMinZ = chunkZ * chunkSize;
        for (int x = chunkMinX; x < chunkMinX + chunkSize; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + chunkSize; z++) {
                chunk.attachChild(new Voxel(x, (int) heightMap.getHeight(x, z), z, assetManager));
            }
        }
        return chunk;
    }
}
