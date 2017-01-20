package nl.civcraft.core.interaction.util;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 4-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface CurrentVoxelHighlighter {
    GameObject highLight();

    GameObject getCurrentVoxel();

    void clear();
}
