package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.VoxelFace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends AbstractGameComponent implements Breakable {

    private final String type;
    private final Map<Face, VoxelFace> faces;
    private final VoxelManager voxelManager;

    public Voxel(String type, VoxelManager voxelManager) {
        this.type = type;
        this.voxelManager = voxelManager;
        this.faces = new HashMap<>();
        for (Face face : Face.values()) {
            faces.put(face, new VoxelFace());
        }
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        voxelManager.addVoxel(gameObject);
    }

    @Override
    public void destroyed(GameObject gameObject) {
        super.destroyed(gameObject);
        voxelManager.removeVoxel(gameObject);
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
