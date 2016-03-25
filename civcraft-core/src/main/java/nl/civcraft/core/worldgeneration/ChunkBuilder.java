package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelProducer;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChunkBuilder {

    private final VoxelProducer dirtVoxelProducer;
    private final VoxelProducer cobbleVoxelProducer;
    private final VoxelProducer grassVoxelProducer;

    @Autowired
    public ChunkBuilder(VoxelProducer dirtVoxelProducer, VoxelProducer cobbleVoxelProducer, VoxelProducer grassVoxelProducer) {
        this.dirtVoxelProducer = dirtVoxelProducer;
        this.cobbleVoxelProducer = cobbleVoxelProducer;
        this.grassVoxelProducer = grassVoxelProducer;
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
                        voxel = cobbleVoxelProducer.produce(x, y, z);
                    } else {
                        if (voxelY == y) {
                            voxel = grassVoxelProducer.produce(x, y, z);
                        } else {
                            voxel = dirtVoxelProducer.produce(x, y, z);
                        }
                    }
                    voxels.add(voxel);
                }
            }
        }
    }
}
