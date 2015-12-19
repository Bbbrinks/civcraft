package nl.civcraft.core.worldgeneration;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class ChunkBuilder {
    @Autowired
    private AssetManager assetManager;


    private static final int CHUNK_SIZE = 10;

    public Chunk buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        Chunk chunk = new Chunk(CHUNK_SIZE, new ChunkLodOptimizerControl());
        int chunkMinX = chunkX * CHUNK_SIZE;
        int chunkMinZ = chunkZ * CHUNK_SIZE;
        for (int x = chunkMinX; x < chunkMinX + CHUNK_SIZE; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + CHUNK_SIZE; z++) {
                String type;
                long rnd = MathUtil.rnd(1);
                Material mat1 = new Material(assetManager,
                        "Common/MatDefs/Misc/Unshaded.j3md");
                if (rnd == 0) {
                    type = "brown";
                    mat1.setColor("Color", ColorRGBA.Brown);
                } else {
                    type = "white";
                    mat1.setColor("Color", ColorRGBA.White);
                }
                int voxelY = (int) heightMap.getHeight(x, z);
                Voxel voxel = new Voxel(x, voxelY, z, type, mat1);
                chunk.addVoxel(voxel);
            }
        }
        return chunk;
    }
}
