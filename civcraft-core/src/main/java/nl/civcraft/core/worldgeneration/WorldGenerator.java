package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.Chunk;
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
    private final PrefabManager blockManager;
    private HeightMap heightMap;
    private boolean generationDone;

    private final static int WORLD_WIDTH = 3;
    private final static int WORLD_DEPTH = 3;

    @Inject
    public WorldGenerator(@Named("height_map_width") int heightMapWidth,
                          @Named("height_map_height") int heightMapHeight,
                          HeightMapGenerator hillsGenerator,
                          ChunkBuilder chunkBuilder,
                          TreeGenerator treeGenerator,
                          @Named("civvy") PrefabManager civvyManager,
                          @Named("block") PrefabManager blockManager,
                          VoxelManager voxelManager) {
        this.heightMapWidth = heightMapWidth;
        this.heightMapHeight = heightMapHeight;
        this.hillsGenerator = hillsGenerator;
        this.chunkBuilder = chunkBuilder;
        this.treeGenerator = treeGenerator;
        this.civvyManager = civvyManager;
        this.voxelManager = voxelManager;
        this.blockManager = blockManager;
    }

    @Override
    public void run() {
        LOGGER.trace("Start generating height map");

        generateHeightMap();
        LOGGER.trace("End generating height map");
        voxelManager.clear();
        blockManager.pausePublishing();
        int chunkCount = 0;
        for (int x = 0; x < WORLD_WIDTH; x++) {
            for (int z = 0; z < WORLD_DEPTH; z++) {
                generateChunk(x, z);
                LOGGER.trace("Generating chunk: " + (chunkCount  + 1) + "/" + WORLD_WIDTH * WORLD_DEPTH);
                chunkCount++;
            }
        }
        for (int i = 0; i < WORLD_WIDTH * WORLD_DEPTH * 7; i++) {
            float treeX = MathUtil.rnd(0f, Chunk.CHUNK_SIZE * (float)WORLD_WIDTH);
            float treeZ = MathUtil.rnd(0f, Chunk.CHUNK_SIZE * (float)WORLD_DEPTH);
            float treeY = heightMap.getHeight((int) treeX, (int) treeZ);
            treeGenerator.addTree((int) treeX, (int) treeY, (int) treeZ);

        }

        for (int i = 0; i < 2; i++) {
            float civX = MathUtil.rnd(0f, Chunk.CHUNK_SIZE * (float)WORLD_WIDTH);
            float civZ = MathUtil.rnd(0f, Chunk.CHUNK_SIZE * (float)WORLD_DEPTH);
            float civY = heightMap.getHeight((int) civX, (int) civZ);
            Optional<GameObject> voxelAt = voxelManager.getVoxelAt(civX, civY, civZ);
            if (!voxelAt.isPresent()) {
                throw new IllegalStateException("Voxel not present");
            }
            Matrix4f transform = new Matrix4f();
            transform.setTranslation(new Vector3f(civX, civY + 1, civZ));
            civvyManager.build(transform, true);
        }
        blockManager.resumePublishing();
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
