package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.VoxelRenderer;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends AbstractGameComponent implements Breakable, Renderable {

    private final String type;
    private final VoxelManager voxelManager;
    private final VoxelRenderer.StaticVoxelRendererFactory voxelRenderer;
    private boolean placed = false;

    public Voxel(String type, VoxelManager voxelManager, VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
        this.type = type;
        this.voxelManager = voxelManager;
        this.voxelRenderer = voxelRenderer;
    }

    @Override
    public void destroyed() {
        voxelManager.removeVoxel(gameObject);
        super.destroyed();
    }

    public void place() {
        if (!placed) {
            placed = true;
            gameObject.addComponent(voxelRenderer.build());
            voxelManager.addVoxel(gameObject);
        } else {
            throw new IllegalStateException("Allready placed");
        }
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean damageMe(GameObject civvy) {
        if (placed) {
            gameObject.removeComponent(VoxelRenderer.class);
            placed = false;
            return true;
        } else {
            throw new IllegalStateException("Not placed");
        }
    }

    @Override
    public String getGeometryName() {
        return type;
    }

    public static class Factory implements GameComponentFactory<Voxel> {
        private final String type;
        private final VoxelManager voxelManager;
        private final VoxelRenderer.StaticVoxelRendererFactory voxelRenderer;

        public Factory(String type, VoxelManager voxelManager, VoxelRenderer.StaticVoxelRendererFactory voxelRenderer) {
            this.type = type;
            this.voxelManager = voxelManager;
            this.voxelRenderer = voxelRenderer;
        }

        @Override
        public Voxel build() {
            return new Voxel(type, voxelManager, voxelRenderer);
        }

        @Override
        public Class<Voxel> getComponentType() {
            return Voxel.class;
        }
    }
}
