package nl.civcraft.core.worldgeneration;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class WorldGenerator {

    @Autowired
    private HillsGenerator hillsGenerator;
    @Autowired
    private ChunkBuilder chunkBuilder;
    private HeightMap heightMap;


    public Chunk generateChunk(int chunkX, int chunkZ) {
        return chunkBuilder.buildChunk(chunkX, chunkZ, heightMap);
    }

    public void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(1000, 1000);
    }
}
