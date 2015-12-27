package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.World;
import nl.civcraft.core.rendering.RenderedVoxelFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    @Autowired
    private WorldManager worldManager;
    private final List<RenderedVoxelFilter> renderedVoxelFilters;

    public WorldGenerator(int heightMapWidth, int heightMapHeight, List<RenderedVoxelFilter> renderedVoxelFilters) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.renderedVoxelFilters = renderedVoxelFilters;
    }


    public void generateChunk(int chunkX, int chunkZ, List<RenderedVoxelFilter> renderedVoxelFilters) {
        chunkBuilder.buildChunk(chunkX, chunkZ, heightMap, worldManager.getWorld(), renderedVoxelFilters);
    }

    public void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }

    @Override
    public void run() {
        DebugStatsState.LAST_MESSAGE = "Start generating height map";
        LOGGER.trace(DebugStatsState.LAST_MESSAGE);

        generateHeightMap();
        DebugStatsState.LAST_MESSAGE = "End generating height map";
        LOGGER.trace(DebugStatsState.LAST_MESSAGE);
        worldManager.getWorld().clearChunks();
        int chunkCount = 0;
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                generateChunk(x, z, renderedVoxelFilters);
                DebugStatsState.LAST_MESSAGE = "Generating chunk: " + chunkCount + "/100";
                LOGGER.trace(DebugStatsState.LAST_MESSAGE);
                chunkCount++;
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

    public World getWorld() {
        return worldManager.getWorld();
    }
}
