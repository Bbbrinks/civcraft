package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.debug.DebugStatsState;
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
        DebugStatsState.LAST_MESSAGE = "Start generating height map";
        LOGGER.trace(DebugStatsState.LAST_MESSAGE);

        generateHeightMap();
        DebugStatsState.LAST_MESSAGE ="End generating height map";
        LOGGER.trace(DebugStatsState.LAST_MESSAGE);

        chunks = new ArrayList<>();
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                chunks.add(generateChunk(x, z));
                DebugStatsState.LAST_MESSAGE = "Generating chunk: " + chunks.size() + "/100";
                LOGGER.trace(DebugStatsState.LAST_MESSAGE );
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
