package nl.civcraft.core.worldgeneration;

import com.jme3.scene.Geometry;
import nl.civcraft.core.managers.VoxelGeometryManager;
import nl.civcraft.core.model.Chunk;
import nl.civcraft.core.model.Voxel;
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
    public VoxelGeometryManager voxelGeometryManager;

    public Chunk buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        Chunk chunk = new Chunk(CHUNK_SIZE, chunkX, chunkZ, new ChunkLodOptimizerControl());
        int chunkMinX = chunkX * CHUNK_SIZE;
        int chunkMinZ = chunkZ * CHUNK_SIZE;
        for (int x = chunkMinX; x < chunkMinX + CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + CHUNK_SIZE; z++) {
                int voxelY = (int) heightMap.getHeight(x, z);
                for (int y = 0; y <= voxelY; y++) {
                    String type;
                    long rnd = MathUtil.rnd(2)-1;
                    if (voxelY - y - rnd   > 0) {
                        type = "cobble";
                    } else {
                        type = "dirt";
                    }
                    Geometry cobble = voxelGeometryManager.findBlock(type);
                    Voxel voxel = new Voxel(x, y, z, type, cobble);
                    chunk.addVoxel(voxel);
                }
            }
        }
        return chunk;
    }
}
