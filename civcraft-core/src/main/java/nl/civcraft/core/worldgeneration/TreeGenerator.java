package nl.civcraft.core.worldgeneration;


import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;

public class TreeGenerator {


    private final PrefabManager treeTrunk;
    private final PrefabManager treeLeaf;

    @Inject
    public TreeGenerator(@Named("treeTrunk") PrefabManager treeTrunk,
                         @Named("treeLeaf") PrefabManager
                                 treeLeaf) {
        this.treeLeaf = treeLeaf;
        this.treeTrunk = treeTrunk;

    }


    public void addTree(int treeX,
                        int treeY,
                        int treeZ) {
        long rnd = MathUtil.rnd(2, 5);
        for (int i = 1; i <= rnd; i++) {
            Matrix4f transform = new Matrix4f();
            transform.setTranslation(new Vector3f(treeX, treeY + i, treeZ));
            treeTrunk.build(transform, true);
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int leafX = treeX - 2 + j;
                    int leafZ = treeZ - 2 + k;
                    int leafY = (int) (treeY + rnd + i);
                    Matrix4f transform = new Matrix4f();
                    transform.setTranslation(new Vector3f(leafX, leafY, leafZ));
                    treeLeaf.build(transform, true);
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                int leafX = treeX - 1 + j;
                int leafZ = treeZ - 1 + k;
                int leafY = (int) (treeY + rnd + 2);
                Matrix4f transform = new Matrix4f();
                transform.setTranslation(new Vector3f(leafX, leafY, leafZ));
                treeLeaf.build(transform, true);
            }
        }
    }

}
