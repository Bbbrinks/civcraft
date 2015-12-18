package nl.civcraft.core.worldgeneration;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class WorldGenerator {

    private final int heightMapWidth;
    private final int heightMapHeight;
    @Autowired
    private HillsGenerator hillsGenerator;
    @Autowired
    private ChunkBuilder chunkBuilder;
    private HeightMap heightMap;

    public WorldGenerator(int heightMapWidth, int heightMapHeight) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
    }


    public Chunk generateChunk(int chunkX, int chunkZ) {
        return chunkBuilder.buildChunk(chunkX, chunkZ, heightMap);
    }

    public void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }
}
