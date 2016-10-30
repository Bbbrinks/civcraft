package nl.civcraft.core.gamecomponents;

import com.jme3.scene.Node;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class VoxelRenderer extends AbstractGameComponent {
    protected Voxel voxel;

    @Override
    public void addTo(GameObject gameObject) {
        Optional<Voxel> component = gameObject.getComponent(Voxel.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("VoxelRenderer can only be applied to Voxels");
        }
        this.voxel = component.get();
        super.addTo(gameObject);
    }

    public abstract Node getNode();

    public Voxel getVoxel() {
        return voxel;
    }
}
