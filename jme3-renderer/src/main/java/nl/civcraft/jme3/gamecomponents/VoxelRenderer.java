package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.VoxelFace;

import java.util.Map;
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

    public abstract Map<Face, VoxelFace> getFaces();

    public Voxel getVoxel() {
        return voxel;
    }
}
