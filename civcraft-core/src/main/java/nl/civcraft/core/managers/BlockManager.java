package nl.civcraft.core.managers;

import nl.civcraft.core.model.Block;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


public class BlockManager {

    @Autowired
    public List<Block> blocks;


    public Block findBlock(String blockName) {
        return blocks.stream().filter(g -> g.getName().equals(blockName)).limit(1).collect(Collectors.toList()).get(0);
    }
}
