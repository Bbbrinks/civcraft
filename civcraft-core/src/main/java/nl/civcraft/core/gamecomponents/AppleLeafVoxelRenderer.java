package nl.civcraft.core.gamecomponents;

import com.jme3.scene.Node;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class AppleLeafVoxelRenderer extends VoxelRenderer {
    private final Node treeLeafBlock;
    private final Node appleTreeLeafBlock;

    public AppleLeafVoxelRenderer(Node treeLeafBlock, Node appleTreeLeafBlock) {
        this.treeLeafBlock = treeLeafBlock;
        this.appleTreeLeafBlock = appleTreeLeafBlock;
    }

    @Override
    public Node getNode() {
        Optional<Inventory> component = voxel.getGameObject().getComponent(Inventory.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("No inventory in game object");
        }
        if (component.get().isEmpty()) {
            return (Node) treeLeafBlock.clone();
        } else {
            return (Node) appleTreeLeafBlock.clone();
        }
    }
}
