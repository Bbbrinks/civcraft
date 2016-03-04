package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkBuilder {


    @Autowired
    public BlockManager blockManager;

    public void buildChunk(int chunkX, int chunkZ, HeightMap heightMap, World world) {
        int chunkMinX = chunkX * World.CHUNK_SIZE;
        int chunkMinZ = chunkZ * World.CHUNK_SIZE;
        List<Voxel> voxels = new ArrayList<>();
        for (int x = chunkMinX; x < chunkMinX + World.CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + World.CHUNK_SIZE; z++) {
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
                    voxels.add(voxel);
                }
            }
        }

        world.addVoxels(voxels);
    }
}
