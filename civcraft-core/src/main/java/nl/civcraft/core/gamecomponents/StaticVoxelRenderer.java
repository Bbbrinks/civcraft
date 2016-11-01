package nl.civcraft.core.gamecomponents;


import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.VoxelFace;

import java.util.Map;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StaticVoxelRenderer extends VoxelRenderer {

    private final Map<Face, VoxelFace> block;

    public StaticVoxelRenderer(Map<Face, VoxelFace> block) {
        this.block = block;

    }

    @Override
    public Map<Face, VoxelFace> getFaces() {
        return block;
    }

}
