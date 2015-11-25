package nl.civcraft.core.worldgeneration;

import com.jme3.asset.AssetManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class WorldGenerator {
    @Autowired
    private AssetManager assetManager;
    @Autowired
    private RollingHillsGenerator rollingHillsGenerator;

    public Chunk generateChunk(int chunkX, int chunkY, int chunkZ) {
        Chunk chunk = new Chunk();

        HeightMap heightMap = rollingHillsGenerator.generateRandomHeightMap(100, 100);
        for (int x = 0; x < heightMap.getWidth(); x++) {
            for (int z = 0; z < heightMap.getLength(); z++) {
                chunk.attachChild(new Voxel(x + chunkX, (int) heightMap.getHeight(x, z), z + chunkZ, assetManager));
            }
        }
        return chunk;
    }
}
