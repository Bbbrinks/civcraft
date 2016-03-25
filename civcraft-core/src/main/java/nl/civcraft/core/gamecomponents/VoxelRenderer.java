package nl.civcraft.core.gamecomponents;

import com.jme3.scene.Node;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class VoxelRenderer implements GameComponent {
    protected Voxel voxel;

    @Override
    public void addTo(GameObject gameObject) {
        if (!(gameObject instanceof Voxel)) {
            throw new RuntimeException("VoxelRenderer can only be applied to Voxels");
        }
        this.voxel = (Voxel) gameObject;
    }

    public abstract Node getNode();

    public Voxel getVoxel() {
        return voxel;
    }
}
