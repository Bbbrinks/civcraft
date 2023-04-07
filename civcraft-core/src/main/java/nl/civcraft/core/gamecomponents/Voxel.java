package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends Neighbour implements Breakable, Renderable {

    private final String type;

    public Voxel(String type,
                 VoxelManager voxelManager) {
        super(voxelManager);
        this.type = type;
    }

    @Override
    public void addTo(GameObject gameObject) {
        voxelManager.addVoxel(gameObject);
        super.addTo(gameObject);

    }

    @Override
    public void destroyed() {
        getNeighbours().forEach((neighbourDirection, gameObject1) -> gameObject1.changed());
        voxelManager.removeVoxel(gameObject);
        super.destroyed();
    }


    public String getType() {
        return type;
    }

    @Override
    public boolean damageMe(GameObject civvy) {
        getGameObject().destroy();
        return true;
    }

    @Override
    public String getGeometryName() {
        return type;
    }

    public boolean isVisible() {
        return getDirectNeighbours().size() < 6;
    }

    public static class Factory implements GameComponentFactory<Voxel> {
        private final String type;
        private final VoxelManager voxelManager;

        public Factory(String type,
                       VoxelManager voxelManager) {
            this.type = type;
            this.voxelManager = voxelManager;
        }

        @Override
        public Voxel build() {
            return new Voxel(type, voxelManager);
        }

        @Override
        public Class<Voxel> getComponentType() {
            return Voxel.class;
        }
    }
}
