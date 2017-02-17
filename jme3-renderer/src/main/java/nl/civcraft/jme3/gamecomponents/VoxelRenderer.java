package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.model.RenderedVoxelFace;
import nl.civcraft.jme3.rendering.VoxelRendererControl;

import java.util.Map;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class VoxelRenderer extends AbstractGameComponent implements nl.civcraft.core.rendering.VoxelRenderer {
    private final VoxelRendererControl voxelRendererControl;
    private Voxel voxel;

    protected VoxelRenderer(VoxelRendererControl voxelRendererControl) {
        this.voxelRendererControl = voxelRendererControl;
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        voxelRendererControl.handleVoxelAdded(this);
    }

    @Override
    public void removeFrom(GameObject gameObject) {
        voxelRendererControl.handleVoxelRemoved(this);
        super.removeFrom(gameObject);
    }

    public abstract Map<Face, RenderedVoxelFace> getFaces();

    public Voxel getVoxel() {
        if (voxel != null) {
            return voxel;
        }
        Optional<Voxel> component = gameObject.getComponent(Voxel.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("VoxelRenderer can only be applied to Voxels");
        }
        voxel = component.get();
        return voxel;
    }

    public boolean isVisible() {
        return gameObject.getComponent(Neighbour.class).map(n -> n.getDirectNeighbours().size() < 6)
                .orElse(true);
    }
}
