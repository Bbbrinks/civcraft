package nl.civcraft.core.gamecomponents;

import com.jme3.math.Vector3f;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 18-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GroundMovement extends AbstractGameComponent {
    private final float speed;
    private final VoxelManager voxelManager;
    private GameObject currentVoxel;

    public GroundMovement(float speed, VoxelManager voxelManager) {
        this.speed = speed;
        this.voxelManager = voxelManager;
    }

    public GameObject currentVoxel() {
        return currentVoxel;
    }

    public void moveToward(GameObject target, float tpf) {
        Optional<Voxel> voxelOptional = target.getComponent(Voxel.class);
        if (!voxelOptional.isPresent()) {
            throw new IllegalStateException("Can only move toward voxels");
        }
        Vector3f location = target.getTransform().getTranslation().add(new Vector3f(0, 1, 0));
        Vector3f movement = location.subtract(gameObject.getTransform().getTranslation());
        if (distance(target) >= tpf * speed) {
            movement.normalizeLocal();
            movement = movement.mult(tpf * speed);

        } else {
            currentVoxel = target;
        }

        gameObject.getTransform().setTranslation(gameObject.getTransform().getTranslation().add(movement));
    }

    public float distance(GameObject target) {
        return target.getTransform().getTranslation().distance(gameObject.getTransform().getTranslation().subtract(0, 1, 0));
    }


    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);

        GameObject groundVoxel = voxelManager.getGroundAt(gameObject.getTransform().getTranslation(), 10).
                map(v -> v).
                orElseThrow(() -> new IllegalStateException("No ground found at " + gameObject.getTransform().getTranslation()));

        setCurrentVoxel(groundVoxel);
    }

    public GameObject getCurrentVoxel() {
        return currentVoxel;
    }

    public void setCurrentVoxel(GameObject currentVoxel) {
        this.currentVoxel = currentVoxel;
    }

    public static class Factory implements GameComponentFactory<GroundMovement> {
        private final float speed;
        private final VoxelManager voxelManager;

        public Factory(float speed, VoxelManager voxelManager) {
            this.speed = speed;
            this.voxelManager = voxelManager;
        }

        @Override
        public GroundMovement build() {
            return new GroundMovement(speed, voxelManager);
        }

        @Override
        public Class<GroundMovement> getComponentType() {
            return GroundMovement.class;
        }
    }
}
