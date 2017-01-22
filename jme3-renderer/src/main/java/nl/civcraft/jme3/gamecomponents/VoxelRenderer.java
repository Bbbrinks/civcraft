package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.model.RenderedVoxelFace;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class VoxelRenderer extends AbstractGameComponent implements nl.civcraft.core.rendering.VoxelRenderer {
    Voxel voxel;

    @Override
    public void addTo(GameObject gameObject) {
        Optional<Voxel> component = gameObject.getComponent(Voxel.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("VoxelRenderer can only be applied to Voxels");
        }
        this.voxel = component.get();
        super.addTo(gameObject);
    }

    public abstract Map<Face, RenderedVoxelFace> getFaces();

    public Voxel getVoxel() {
        return voxel;
    }

    public boolean isVisible() {
        return gameObject.getComponent(Neighbour.class).map(n -> n.getNeighbours().size() < 6).orElse(true);
    }
}
