package nl.civcraft.core.worldgeneration;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ChunkBuilder {


    private final PrefabManager cobbleStone;
    private final PrefabManager grass;
    private final PrefabManager dirt;

    @Autowired
    public ChunkBuilder(@Qualifier("cobbleStone") PrefabManager cobbleStone, @Qualifier("grass") PrefabManager
            grass, @Qualifier("dirt") PrefabManager dirt) {
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
                for (int y = 0; y <= voxelY; y++) {
                    Transform transform = new Transform(new Vector3f(x, y, z));
                    long rnd = MathUtil.rnd(8) - 1;
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
                    build.getComponent(Voxel.class).ifPresent(Voxel::place);
                }
            }
        }
    }
}
