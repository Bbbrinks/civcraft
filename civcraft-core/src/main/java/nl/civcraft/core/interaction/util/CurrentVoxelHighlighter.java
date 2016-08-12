package nl.civcraft.core.interaction.util;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CurrentVoxelHighlighter {

    private final Camera camera;
    private final InputManager inputManager;
    private final Node rootNode;
    private final WorldManager worldManager;
    private final Spatial hoverSpatial;
    private Node hoverBoxes;
    private Voxel voxelAt;

    @Autowired
    public CurrentVoxelHighlighter(Camera camera, InputManager inputManager, Node rootNode, WorldManager worldManager, Spatial hoverSpatial) {
        this.camera = camera;
        this.inputManager = inputManager;
        this.rootNode = rootNode;
        this.worldManager = worldManager;
        this.hoverSpatial = hoverSpatial;
        hoverBoxes = new Node("hoverBoxes");
        rootNode.attachChild(hoverBoxes);
    }

    public Voxel highLight() {
        Voxel voxelAt = getCurrentVoxel();
        if (voxelAt != null) {
            Voxel currentVoxel = voxelAt;
            hoverBoxes.detachAllChildren();
            Spatial clone = hoverSpatial.clone();
            clone.setLocalTranslation(clone.getLocalTranslation().x + currentVoxel.getX(), clone.getLocalTranslation().y + currentVoxel.getY(), clone.getLocalTranslation().z + currentVoxel.getZ());
            hoverBoxes.attachChild(clone);
        }
        return voxelAt;
    }

    public Voxel getCurrentVoxel() {
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = camera.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        rootNode.collideWith(ray, results);
        if (results.size() > 0) {
            Vector3f contactPoint = results.getCollision(0).getContactPoint();
            int x = Math.round(contactPoint.x);
            int y = Math.round(contactPoint.y);
            int z = Math.round(contactPoint.z);
            Voxel newVoxel = worldManager.getWorld().getVoxelAt(x, y, z);
            if (newVoxel != null) {
                this.voxelAt = newVoxel;
            }
        }
        return voxelAt;
    }

    public void clear() {
        hoverBoxes.detachAllChildren();
    }
}
