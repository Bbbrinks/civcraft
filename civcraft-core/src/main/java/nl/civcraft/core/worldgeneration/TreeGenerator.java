package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.blocks.TreeLeaf;
import nl.civcraft.core.blocks.TreeTrunk;
import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.World;
import nl.civcraft.core.utils.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TreeGenerator {

    private final BlockManager blockManager;

    @Autowired
    public TreeGenerator(BlockManager blockManager) {
        this.blockManager = blockManager;
    }


    public void addTree(int treeX, int treeY, int treeZ, World world) {
        long rnd = MathUtil.rnd(2, 5);
        List<Voxel> voxels = new ArrayList<>();
        for (int i = 1; i <= rnd; i++) {
            voxels.add(blockManager.createVoxel(TreeTrunk.BLOCK_NAME, treeX, treeY + i, treeZ));
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int leafX = treeX - 2 + j;
                    int leafZ = treeZ - 2 + k;
                    int leafY = (int) (treeY + rnd + i);
                    if (!voxels.stream().filter(v -> v.getX() == leafX && v.getY() == leafY && v.getZ() == leafZ).findFirst().isPresent()) {
                        voxels.add(blockManager.createVoxel(TreeLeaf.BLOCK_NAME, leafX, leafY, leafZ));
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
                    voxels.add(blockManager.createVoxel(TreeLeaf.BLOCK_NAME, leafX, leafY, leafZ));
                }
            }
        }

        world.addVoxels(voxels);
    }

}
