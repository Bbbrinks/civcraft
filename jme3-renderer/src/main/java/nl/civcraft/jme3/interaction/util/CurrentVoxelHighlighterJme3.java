package nl.civcraft.jme3.interaction.util;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrentVoxelHighlighterJme3 implements CurrentVoxelHighlighter {

    public static final float EPSILON = 0.0001f;
    private final Camera camera;
    private final InputManager inputManager;
    private final Node rootNode;
    private final VoxelManager voxelManager;
    private final Spatial hoverSpatial;
    private Node hoverBoxes;
    private GameObject voxelAt;

    @Autowired
    public CurrentVoxelHighlighterJme3(Camera camera, InputManager inputManager, Node rootNode, VoxelManager voxelManager, Spatial hoverSpatial) {
        this.camera = camera;
        this.inputManager = inputManager;
        this.rootNode = rootNode;
        this.voxelManager = voxelManager;
        this.hoverSpatial = hoverSpatial;
        hoverBoxes = new Node("hoverBoxes");
        rootNode.attachChild(hoverBoxes);
    }

    @Override
    public GameObject highLight() {
        GameObject voxelAt = getCurrentVoxel();
        if (voxelAt != null) {
            GameObject currentVoxel = voxelAt;
            hoverBoxes.detachAllChildren();
            Spatial clone = hoverSpatial.clone();
            clone.setLocalTranslation(currentVoxel.getTransform().getTranslation());
            hoverBoxes.attachChild(clone);
        }
        return voxelAt;
    }

    @Override
    public GameObject getCurrentVoxel() {
        Spatial voxelNode = rootNode.getChild("chunks");
        if (voxelNode == null) {
            return null;
        }
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = camera.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = camera.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        voxelNode.collideWith(ray, results);
        if (results.size() > 0) {
            for (int i = 0; i < 2 && i < results.size(); i++) {

                Vector3f contactPoint = results.getCollision(i).getContactPoint();
                int y = (int) contactPoint.y;
                if (Math.abs(contactPoint.y - y - 0.5f) < EPSILON) {
                    y = Math.round(contactPoint.y - 0.1f);
                } else {
                    y = Math.round(contactPoint.y);
                }
                int x = Math.round(contactPoint.x);
                int z = Math.round(contactPoint.z);
                Optional<GameObject> newVoxel = voxelManager.getVoxelAt(x, y, z);
                if (newVoxel.isPresent()) {
                    this.voxelAt = newVoxel.get();
                    break;
                }
            }
        }
        return voxelAt;
    }

    @Override
    public void clear() {
        hoverBoxes.detachAllChildren();
    }
}
