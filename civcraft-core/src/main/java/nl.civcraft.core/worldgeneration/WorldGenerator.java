package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.utils.MathUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;


public class WorldGenerator implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final int heightMapWidth;
    private final int heightMapHeight;


    private final HeightMapGenerator hillsGenerator;

    private final ChunkBuilder chunkBuilder;
    private final TreeGenerator treeGenerator;
    private final PrefabManager civvyManager;
    private final VoxelManager voxelManager;
    private HeightMap heightMap;
    private boolean generationDone;

    @Inject
    public WorldGenerator(@Named("height_map_width") int heightMapWidth,
                          @Named("height_map_height") int heightMapHeight,
                          HeightMapGenerator hillsGenerator,
                          ChunkBuilder chunkBuilder,
                          TreeGenerator treeGenerator,
                          @Named("civvy") PrefabManager civvyManager,
                          VoxelManager voxelManager) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.hillsGenerator = hillsGenerator;
        this.chunkBuilder = chunkBuilder;
        this.treeGenerator = treeGenerator;
        this.civvyManager = civvyManager;
        this.voxelManager = voxelManager;
    }

    @Override
    public void run() {
        LOGGER.trace("Start generating height map");

        generateHeightMap();
        LOGGER.trace("End generating height map");
        voxelManager.clear();
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
            Optional<GameObject> voxelAt = voxelManager.getVoxelAt(civX, civY, civZ);
            if (!voxelAt.isPresent()) {
                throw new IllegalStateException("Voxel not present");
            }
            Matrix4f transform = new Matrix4f();
            transform.setTranslation(new Vector3f(civX, civY + 1, civZ));
            civvyManager.build(transform, true);
        }
        setGenerationDone(true);
    }

    private void generateHeightMap() {
        this.heightMap = hillsGenerator.generateRandomHeightMap(heightMapWidth, heightMapHeight);
    }

    private void generateChunk(int chunkX,
                               int chunkZ) {
        chunkBuilder.buildChunk(chunkX, chunkZ, heightMap);
    }

    public boolean isGenerationDone() {
        return generationDone;
    }

    @SuppressWarnings("SameParameterValue")
    private void setGenerationDone(boolean generationDone) {
        this.generationDone = generationDone;
    }

}
