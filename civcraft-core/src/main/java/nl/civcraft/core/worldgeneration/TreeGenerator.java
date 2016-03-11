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
 * Created by Bob on 11-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class TreeGenerator {

    @Autowired
    public BlockManager blockManager;

    public void addTree(int treeX, int treeY, int treeZ, World world) {
        Block treeTrunk = blockManager.findBlock("treeTrunk");
        long rnd = MathUtil.rnd(1, 5);
        List<Voxel> voxels = new ArrayList<>();
        for (int i = 1; i <= rnd; i++) {
            voxels.add(new Voxel(treeX, treeY + i, treeZ, "treeTrunk", treeTrunk));
        }
        Block treeLeaf = blockManager.findBlock("treeLeaf");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int leafX = treeX - 2 + j;
                    int leafZ = treeZ - 2 + k;
                    int leafY = (int) (treeY + rnd + i);
                    if (!voxels.stream().filter(v -> v.getX() == leafX && v.getY() == leafY && v.getZ() == leafZ).findFirst().isPresent()) {
                        voxels.add(new Voxel(leafX, leafY, leafZ, "treeLeaf", treeLeaf));
                    }
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                int leafX = treeX - 1 + j;
                int leafZ = treeZ - 1 + k;
                int leafY = (int) (treeY + rnd + 2);
                if (!voxels.stream().filter(v -> v.getX() == leafX && v.getY() == leafY && v.getZ() == leafZ).findFirst().isPresent()) {
                    voxels.add(new Voxel(leafX, leafY, leafZ, "treeLeaf", treeLeaf));
                }
            }
        }

        world.addVoxels(voxels);
    }
}
