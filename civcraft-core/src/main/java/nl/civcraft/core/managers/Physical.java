package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Physical implements GameComponent {
    private final Voxel currentVoxel;
    private Transform transform;

    public Physical(Voxel groundAt, Vector3f location) {
        this.transform = new Transform(location);
        this.currentVoxel = groundAt;
    }

    public Voxel getCurrentVoxel() {
        return currentVoxel;
    }

    @Override
    public void addTo(GameObject gameObject) {

    }

    @Override
    public void destroyed(GameObject gameObject) {

    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
