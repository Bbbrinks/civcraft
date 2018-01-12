package nl.civcraft.opengl.interaction;

import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CurrentVoxelHighlighter implements nl.civcraft.core.interaction.util.CurrentVoxelHighlighter {

    private GameObject currentVoxel;

    @Override
    public GameObject highLight() {
        return null;
    }

    @Override
    public Optional<GameObject> getCurrentVoxel() {
        return Optional.ofNullable(currentVoxel);
    }

    @Override
    public void clear() {

    }
}
