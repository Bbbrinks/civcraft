package nl.civcraft.core.model;

import com.jme3.scene.Node;
import nl.civcraft.core.managers.BlockOptimizer;

/**
 * Created by Bob on 20-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Block extends Node {

    private final BlockOptimizer blockOptimizer;

    public Block(String name, BlockOptimizer blockOptimizer) {
        super(name);
        this.blockOptimizer = blockOptimizer;
    }


    public BlockOptimizer getBlockOptimizer() {
        return blockOptimizer;
    }


}
