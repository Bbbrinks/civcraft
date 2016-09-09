package nl.civcraft.core.gamecomponents;


import com.jme3.scene.Node;
import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StaticVoxelRenderer extends VoxelRenderer {

    private final Node block;

    public StaticVoxelRenderer(Node block) {
        this.block = block;

    }

    @Override
    public Node getNode() {
        return (Node) block.clone();
    }

    @Override
    public void destroyed(GameObject gameObject) {

    }
}
