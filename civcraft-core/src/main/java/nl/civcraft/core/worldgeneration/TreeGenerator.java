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
public class TreeGenerator {

    private final VoxelProducer treeTrunkProducer;
    private final VoxelProducer treeLeafProducer;

    @Autowired
    public TreeGenerator(VoxelProducer treeTrunkVoxelProducer, VoxelProducer treeLeafVoxelProducer) {
        this.treeTrunkProducer = treeTrunkVoxelProducer;
        this.treeLeafProducer = treeLeafVoxelProducer;
    }


    public void addTree(int treeX, int treeY, int treeZ, World world) {
        long rnd = MathUtil.rnd(2, 5);
        List<Voxel> voxels = new ArrayList<>();
        for (int i = 1; i <= rnd; i++) {
            voxels.add(treeTrunkProducer.produce(treeX, treeY + i, treeZ));
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int leafX = treeX - 2 + j;
                    int leafZ = treeZ - 2 + k;
                    int leafY = (int) (treeY + rnd + i);
                    if (!voxels.stream().filter(v -> v.getX() == leafX && v.getY() == leafY && v.getZ() == leafZ).findFirst().isPresent()) {
                        voxels.add(treeLeafProducer.produce(leafX, leafY, leafZ));
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
                    voxels.add(treeLeafProducer.produce(leafX, leafY, leafZ));
                }
            }
        }

        world.addVoxels(voxels);
    }

}
