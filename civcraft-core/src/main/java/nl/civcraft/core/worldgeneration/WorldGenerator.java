package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.model.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class WorldGenerator implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int heightMapWidth;
    private final int heightMapHeight;
    @Autowired
    private HillsGenerator hillsGenerator;
    @Autowired
    private ChunkBuilder chunkBuilder;
    private HeightMap heightMap;

    private boolean generationDone;

    public ArrayList<Chunk> getChunks() {
        return chunks;
    }

    private ArrayList<Chunk> chunks;

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

    @Override
    public void run() {
        LOGGER.trace("Start generating height map");
        generateHeightMap();
        LOGGER.trace("End generating height map");
        chunks = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                chunks.add(generateChunk(x, z));
                LOGGER.trace("Generating chunk: " + x + z + "/100");
            }
        }
        generationDone = true;
    }


    public boolean isGenerationDone() {
        return generationDone;
    }

    public void setGenerationDone(boolean generationDone) {
        this.generationDone = generationDone;
    }
}
