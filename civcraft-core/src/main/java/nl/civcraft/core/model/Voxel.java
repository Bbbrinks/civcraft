package nl.civcraft.core.model;

import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.managers.VoxelManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends AbstractGameComponent {

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
