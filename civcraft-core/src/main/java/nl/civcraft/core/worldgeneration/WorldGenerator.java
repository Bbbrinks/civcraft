package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.debug.DebugStatsState;
import nl.civcraft.core.gamecomponents.LimitedInventory;
import nl.civcraft.core.managers.NpcManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.npc.Npc;
import nl.civcraft.core.utils.MathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@PropertySource("classpath:world-generation.properties")
public class WorldGenerator implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int heightMapWidth;
    private final int heightMapHeight;


    private final HeightMapGenerator hillsGenerator;

    private final ChunkBuilder chunkBuilder;
    private final WorldManager worldManager;
    private final ApplicationEventPublisher publisher;
    private final NpcManager civvyManager;
    private final TreeGenerator treeGenerator;
    private HeightMap heightMap;
    private boolean generationDone;

    @Autowired
    public WorldGenerator(@Value("${height_map_width}") int heightMapWidth, @Value("${height_map_height}") int heightMapHeight, HeightMapGenerator hillsGenerator, ChunkBuilder chunkBuilder, WorldManager worldManager, ApplicationEventPublisher publisher, NpcManager civvyManager, TreeGenerator treeGenerator) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.hillsGenerator = hillsGenerator;
        this.chunkBuilder = chunkBuilder;
        this.worldManager = worldManager;
        this.publisher = publisher;
        this.civvyManager = civvyManager;
        this.treeGenerator = treeGenerator;
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
        for (int x = 0; x < 2; x++) {
            for (int z = 0; z < 2; z++) {
                generateChunk(x, z);
                DebugStatsState.LAST_MESSAGE = "Generating chunk: " + chunkCount + "/36";
                LOGGER.trace(DebugStatsState.LAST_MESSAGE);
                chunkCount++;
            }
        }
        for (int i = 0; i < 50; i++) {
            float treeX = MathUtil.rnd(0f, 120f);
            float treeZ = MathUtil.rnd(0f, 120f);
            float treeY = heightMap.getHeight((int) treeX, (int) treeZ);
            treeGenerator.addTree((int) treeX, (int) treeY, (int) treeZ, worldManager.getWorld());

        }

        for (int i = 0; i < 1; i++) {
            float civX = MathUtil.rnd(0f, 120f);
            float civZ = MathUtil.rnd(0f, 120f);
            float civY = heightMap.getHeight((int) civX, (int) civZ);
            Optional<Voxel> voxelAt = worldManager.getWorld().getVoxelAt(civX, civY, civZ);
            if (!voxelAt.isPresent()) {
                throw new IllegalStateException("Voxel not present");
            }
            Npc npc = civvyManager.getNpc("civvy");
            Civvy civvy = new Civvy(civX, civY + 1, civZ, "civvy", npc);
            civvy.setCurrentVoxel(voxelAt.get());
            civvy.addComponent(new LimitedInventory(2));
            worldManager.getWorld().addCivvy(civvy);

        }

        setGenerationDone(true);
    }

    private void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }

    private void generateChunk(int chunkX, int chunkZ) {
        chunkBuilder.buildChunk(chunkX, chunkZ, heightMap, worldManager.getWorld());
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
