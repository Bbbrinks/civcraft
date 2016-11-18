package nl.civcraft.core.worldgeneration;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.managers.GameObjectManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
    private final TreeGenerator treeGenerator;
    private final GameObjectManager civvyManager;
    private HeightMap heightMap;
    private boolean generationDone;

    @Autowired
    public WorldGenerator(@Value("${height_map_width}") int heightMapWidth, @Value("${height_map_height}") int heightMapHeight,
                          HeightMapGenerator hillsGenerator, ChunkBuilder chunkBuilder, WorldManager worldManager, TreeGenerator treeGenerator,
                          @Qualifier(value = "civvy") GameObjectManager civvyManager) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.hillsGenerator = hillsGenerator;
        this.chunkBuilder = chunkBuilder;
        this.worldManager = worldManager;
        this.treeGenerator = treeGenerator;
        this.civvyManager = civvyManager;
    }

    @Override
    public void run() {
        LOGGER.trace("Start generating height map");

        generateHeightMap();
        LOGGER.trace("End generating height map");
        worldManager.getWorld().clearChunks();
        int chunkCount = 0;
        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                generateChunk(x, z);
                LOGGER.trace("Generating chunk: " + chunkCount + "/36");
                chunkCount++;
            }
        }
        for (int i = 0; i < 50; i++) {
            float treeX = MathUtil.rnd(0f, 120f);
            float treeZ = MathUtil.rnd(0f, 120f);
            float treeY = heightMap.getHeight((int) treeX, (int) treeZ);
            treeGenerator.addTree((int) treeX, (int) treeY, (int) treeZ);

        }

        for (int i = 0; i < 1; i++) {
            float civX = MathUtil.rnd(0f, 120f);
            float civZ = MathUtil.rnd(0f, 120f);
            float civY = heightMap.getHeight((int) civX, (int) civZ);
            Optional<Voxel> voxelAt = worldManager.getWorld().getVoxelAt(civX, civY, civZ);
            if (!voxelAt.isPresent()) {
                throw new IllegalStateException("Voxel not present");
            }
            civvyManager.build(new Transform(new Vector3f(civX, civY + 1, civZ)));
        }
        setGenerationDone(true);
    }

    private void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }

    private void generateChunk(int chunkX, int chunkZ) {
        chunkBuilder.buildChunk(chunkX, chunkZ, heightMap);
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
