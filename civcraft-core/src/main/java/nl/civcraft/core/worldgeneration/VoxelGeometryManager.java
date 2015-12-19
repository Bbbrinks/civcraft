package nl.civcraft.core.worldgeneration;

import com.jme3.scene.Geometry;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


public class VoxelGeometryManager {

    @Autowired
    public List<Geometry> blocks;


    public Geometry findBlock(String blockName) {
        return blocks.stream().filter(g -> g.getName().equals(blockName)).limit(1).collect(Collectors.toList()).get(0);
    }
}
