package nl.civcraft.core.interaction.util;

import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 4-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface CurrentVoxelHighlighter {
    Voxel highLight();

    Voxel getCurrentVoxel();

    void clear();
}
