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


    private static final int chunkSize = 10;

    public Chunk buildChunk(int chunkX, int chunkZ, HeightMap heightMap) {
        Chunk chunk = new Chunk(new ChunkLodOptimizerControl());
        int chunkMinX = chunkX * chunkSize;
        int chunkMinZ = chunkZ * chunkSize;
        for (int x = chunkMinX; x < chunkMinX + chunkSize; x++) {
            for (int z = chunkMinZ; z < chunkMinZ + chunkSize; z++) {
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
                chunk.attachChild(new Voxel(x, (int) heightMap.getHeight(x, z), z, type, mat1));
            }
        }
        return chunk;
    }
}
