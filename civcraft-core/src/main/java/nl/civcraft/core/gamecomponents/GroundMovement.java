package nl.civcraft.core.gamecomponents;

import com.jme3.math.Vector3f;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 18-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GroundMovement extends AbstractGameComponent {
    private final float speed;
    private final WorldManager worldManager;
    private Voxel currentVoxel;

    public GroundMovement(float speed, WorldManager worldManager) {
        this.speed = speed;
        this.worldManager = worldManager;
    }

    public Voxel currentVoxel() {
        return currentVoxel;
    }

    public void moveToward(Voxel target, float tpf) {
        Vector3f location = target.getLocation().add(new Vector3f(0, 1, 0));
        Vector3f movement = location.subtract(gameObject.getTransform().getTranslation());
        if (distance(target) >= tpf * speed) {
            movement.normalizeLocal();
            movement = movement.mult(tpf * speed);

        } else {
            currentVoxel = target;
        }

        gameObject.getTransform().setTranslation(gameObject.getTransform().getTranslation().add(movement));
    }

    public float distance(Voxel target) {
        return target.getLocation().distance(gameObject.getTransform().getTranslation().subtract(0, 1, 0));
    }


    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        setCurrentVoxel(worldManager.getWorld().getGroundAt(gameObject.getTransform().getTranslation(), 10).get());
    }

    public Voxel getCurrentVoxel() {
        return currentVoxel;
    }

    public void setCurrentVoxel(Voxel currentVoxel) {
        this.currentVoxel = currentVoxel;
    }

    public static class Factory implements GameComponentFactory<GroundMovement> {
        private final float speed;
        private final WorldManager worldManager;

        public Factory(float speed, WorldManager worldManager) {
            this.speed = speed;
            this.worldManager = worldManager;
        }

        @Override
        public GameComponent build() {
            return new GroundMovement(speed, worldManager);
        }

        @Override
        public Class<GroundMovement> getComponentType() {
            return GroundMovement.class;
        }
    }
}
