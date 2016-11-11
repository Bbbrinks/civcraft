package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChunkBuilder {

    private final BlockManager blockManager;

    @Autowired
    public ChunkBuilder(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    public void buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        int chunkMinX = chunkX * World.CHUNK_SIZE;
        int chunkMinZ = chunkZ * World.CHUNK_SIZE;
        generateTerrain(heightMap, chunkMinX, chunkMinZ);
    }

    private void generateTerrain(HeightMap heightMap, int chunkMinX, int chunkMinZ) {
        for (int x = chunkMinX; x < chunkMinX + World.CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + World.CHUNK_SIZE; z++) {
                int voxelY = (int) heightMap.getHeight(x, z);
                for (int y = 0; y <= voxelY; y++) {

                    long rnd = MathUtil.rnd(8) - 1;
                    if (voxelY - y - rnd > 0) {
                        blockManager.createVoxel("cobbleStone", x, y, z);
                    } else {
                        if (voxelY == y) {
                            blockManager.createVoxel("grass", x, y, z);
                        } else {
                            blockManager.createVoxel("dirt", x, y, z);
                        }
                    }
                }
            }
        }
    }
}
