package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkBuilder {

    private static final int CHUNK_SIZE = 10;

    @Autowired
    public BlockManager blockManager;

    public void buildChunk(int chunkX, int chunkZ, HeightMap heightMap, World world) {
        int chunkMinX = chunkX * CHUNK_SIZE;
        int chunkMinZ = chunkZ * CHUNK_SIZE;
        for (int x = chunkMinX; x < chunkMinX + CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + CHUNK_SIZE; z++) {
                int voxelY = (int) heightMap.getHeight(x, z);
                for (int y = 0; y <= voxelY; y++) {
                    String type;
                    long rnd = MathUtil.rnd(2) - 1;

                    if (voxelY - y - rnd > 0) {
                        type = "cobble";
                    } else {
                        if (voxelY == y) {
                            type = "grass";
                        } else {
                            type = "dirt";
                        }
                    }
                    Block block = blockManager.findBlock(type);
                    Voxel voxel = new Voxel(x, y, z, type, block);
                    world.addVoxel(voxel);
                }
            }
        }
    }
}
