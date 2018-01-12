package nl.civcraft.core.interaction.util;

import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 4-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface CurrentVoxelHighlighter {
    GameObject highLight();

    Optional<GameObject> getCurrentVoxel();

    void clear();
}
