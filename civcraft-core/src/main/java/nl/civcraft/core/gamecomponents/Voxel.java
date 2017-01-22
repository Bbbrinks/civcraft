package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends AbstractGameComponent implements Breakable {

    private final String type;
    private final VoxelManager voxelManager;

    public Voxel(String type, VoxelManager voxelManager) {
        this.type = type;
        this.voxelManager = voxelManager;
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        voxelManager.addVoxel(gameObject);
    }

    @Override
    public void destroyed() {
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

    public static class Factory implements GameComponentFactory<Voxel> {
        private final String type;
        private final VoxelManager voxelManager;

        public Factory(String type, VoxelManager voxelManager) {
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
