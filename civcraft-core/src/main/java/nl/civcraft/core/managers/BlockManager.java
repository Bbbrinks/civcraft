package nl.civcraft.core.managers;

import nl.civcraft.core.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlockManager {


    public final List<Block> blocks;

    @Autowired
    public BlockManager(List<Block> blocks) {
        this.blocks = blocks;
    }


    public Block findBlock(String blockName) {
        return blocks.stream().filter(g -> g.getName().equals(blockName)).findFirst().get();
    }
}
