package nl.civcraft.core.worldgeneration;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.utils.MathUtil;

import javax.inject.Inject;
import javax.inject.Named;


public class ChunkBuilder {


    private final PrefabManager cobbleStone;
    private final PrefabManager grass;
    private final PrefabManager dirt;

    @Inject
    public ChunkBuilder(@Named("cobbleStone") PrefabManager cobbleStone,
                        @Named("grass") PrefabManager
                                grass,
                        @Named("dirt") PrefabManager dirt) {
        this.cobbleStone = cobbleStone;
        this.dirt = dirt;
        this.grass = grass;
    }

    public void buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        int chunkMinX = chunkX * Chunk.CHUNK_SIZE;
        int chunkMinZ = chunkZ * Chunk.CHUNK_SIZE;
        generateTerrain(heightMap, chunkMinX, chunkMinZ);
    }

    private void generateTerrain(HeightMap heightMap, int chunkMinX, int chunkMinZ) {
        for (int x = chunkMinX; x < chunkMinX + Chunk.CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + Chunk.CHUNK_SIZE; z++) {
                int voxelY = (int) heightMap.getHeight(x, z);
                fillToBedrock(x, z, voxelY);
            }
        }
    }

    private void fillToBedrock(int x, int z, int voxelY) {
        for (int y = 0; y <= voxelY; y++) {
            Transform transform = new Transform(new Vector3f(x, y, z));
            int rnd = MathUtil.rnd(8) - 1;
            GameObject build;
            if (voxelY - y - rnd > 0) {
                build = cobbleStone.build(transform, true);
            } else {
                if (voxelY == y) {
                    build = grass.build(transform, true);
                } else {
                    build = dirt.build(transform, true);
                }
            }
            build.getComponent(Voxel.class);
        }
    }
}
