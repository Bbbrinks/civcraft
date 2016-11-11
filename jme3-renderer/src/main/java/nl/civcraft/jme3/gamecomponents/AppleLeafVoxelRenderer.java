package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.model.Face;
import nl.civcraft.jme3.model.VoxelFace;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class AppleLeafVoxelRenderer extends VoxelRenderer {
    private final Map<Face, VoxelFace> treeLeafBlock;
    private final Map<Face, VoxelFace> appleTreeLeafBlock;

    public AppleLeafVoxelRenderer(Map<Face, VoxelFace> treeLeafBlock, Map<Face, VoxelFace> appleTreeLeafBlock) {
        this.treeLeafBlock = treeLeafBlock;
        this.appleTreeLeafBlock = appleTreeLeafBlock;
    }

    @Override
    public Map<Face, VoxelFace> getFaces() {
        Optional<Inventory> component = voxel.getGameObject().getComponent(Inventory.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("No inventory in game object");
        }
        if (component.get().isEmpty()) {
            return treeLeafBlock;
        } else {
            return appleTreeLeafBlock;
        }
    }
}
