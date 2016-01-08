package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.events.CivvyCreated;
import nl.civcraft.core.managers.NpcManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.npc.Npc;
import nl.civcraft.core.rendering.RenderedVoxelFilter;
import nl.civcraft.core.utils.MathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

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
    private final List<RenderedVoxelFilter> renderedVoxelFilters;
    @Autowired
    private HeightMapGenerator hillsGenerator;
    @Autowired
    private ChunkBuilder chunkBuilder;
    private HeightMap heightMap;
    private boolean generationDone;
    @Autowired
    private WorldManager worldManager;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private NpcManager civvyManager;

    public WorldGenerator(int heightMapWidth, int heightMapHeight, List<RenderedVoxelFilter> renderedVoxelFilters) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.renderedVoxelFilters = renderedVoxelFilters;
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
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                generateChunk(x, z, renderedVoxelFilters);
                DebugStatsState.LAST_MESSAGE = "Generating chunk: " + chunkCount + "/36";
                LOGGER.trace(DebugStatsState.LAST_MESSAGE);
                chunkCount++;
            }
        }
        for (int i = 0; i < 3; i++) {
            float civX = MathUtil.rnd(0f, 120f);
            float civZ = MathUtil.rnd(0f, 120f);
            float civY = heightMap.getHeight((int) civX, (int) civZ);
            Voxel voxelAt = worldManager.getWorld().getVoxelAt(civX, civY, civZ);
            Npc npc = civvyManager.getNpc("civvy");
            Civvy civvy = new Civvy(civX, civY + 1, civZ, "civvy", npc);
            civvy.setCurrentVoxel(voxelAt);
            civvy.setWorld(worldManager.getWorld());
            publisher.publishEvent(new CivvyCreated(civvy, this));

        }
        setGenerationDone(true);
    }

    private void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }

    private void generateChunk(int chunkX, int chunkZ, List<RenderedVoxelFilter> renderedVoxelFilters) {
        chunkBuilder.buildChunk(chunkX, chunkZ, heightMap, worldManager.getWorld(), renderedVoxelFilters);
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
