package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.blocks.CobbleStone;
import nl.civcraft.core.blocks.Dirt;
import nl.civcraft.core.blocks.Grass;
import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChunkBuilder {

    private final BlockManager blockManager;

    @Autowired
    public ChunkBuilder(BlockManager blockManager) {
        this.blockManager = blockManager;
    }

    public void buildChunk(int chunkX, int chunkZ, HeightMap heightMap, World world) {
        int chunkMinX = chunkX * World.CHUNK_SIZE;
        int chunkMinZ = chunkZ * World.CHUNK_SIZE;
        List<Voxel> voxels = new ArrayList<>();
        generateTerrain(heightMap, chunkMinX, chunkMinZ, voxels);

        world.addVoxels(voxels);
    }

    private void generateTerrain(HeightMap heightMap, int chunkMinX, int chunkMinZ, List<Voxel> voxels) {
        for (int x = chunkMinX; x < chunkMinX + World.CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + World.CHUNK_SIZE; z++) {
                int voxelY = (int) heightMap.getHeight(x, z);
                for (int y = 0; y <= voxelY; y++) {

                    long rnd = MathUtil.rnd(8) - 1;
                    Voxel voxel;
                    if (voxelY - y - rnd > 0) {
                        voxel = blockManager.createVoxel(CobbleStone.BLOCK_NAME, x, y, z);
                    } else {
                        if (voxelY == y) {
                            voxel = blockManager.createVoxel(Grass.BLOCK_NAME, x, y, z);
                        } else {
                            voxel = blockManager.createVoxel(Dirt.BLOCK_NAME, x, y, z);
                        }
                    }
                    voxels.add(voxel);
                }
            }
        }
    }
}
