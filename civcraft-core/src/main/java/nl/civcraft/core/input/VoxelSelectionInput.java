package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Voxel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 20-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelSelectionInput extends AbstractAppState implements AnalogListener,ActionListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MOUSE_MOTION = "MOUSE_MOTION";
    private static final String DELETE_VOXEL = "DELETE_VOXEL";

    @Autowired
    private Node rootNode;

    @Autowired
    private WorldManager worldManager;

    @Autowired
    private Spatial selectionSpatial;

    @Autowired
    private Spatial hoverSpatial;

    private static final String SELECT_VOXEL = "SELECT_VOXEL";
    private Camera cam;
    private InputManager inputManager;
    private Node selectionBoxes;
    private Node hoverBoxes;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        registerInput(app.getInputManager());
        this.cam = app.getCamera();
        this.inputManager = app.getInputManager();
        selectionBoxes = new Node("selectionBoxes");
        hoverBoxes = new Node("hoverBoxes");
        rootNode.attachChild(selectionBoxes);
        rootNode.attachChild(hoverBoxes);
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(SELECT_VOXEL, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(DELETE_VOXEL, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addMapping(MOUSE_MOTION, new MouseAxisTrigger(MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(MouseInput.AXIS_Y, true), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(this, SELECT_VOXEL,MOUSE_MOTION, DELETE_VOXEL);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed) {
            Voxel voxelAt = getVoxelAt();
            if (voxelAt != null) {
                if(name.equals(SELECT_VOXEL)) {
                    selectionBoxes.detachAllChildren();
                    Spatial clone = selectionSpatial.clone();
                    clone.setLocalTranslation(clone.getLocalTranslation().x + voxelAt.getX(), clone.getLocalTranslation().y + voxelAt.getY(), clone.getLocalTranslation().z + voxelAt.getZ());
                    selectionBoxes.attachChild(clone);
                }
                if(name.equals(DELETE_VOXEL)){
                    selectionBoxes.detachAllChildren();
                    hoverBoxes.detachAllChildren();
                    voxelAt.breakBlock();
                }
            }
        }


    }

    private Voxel getVoxelAt() {
        CollisionResults results = new CollisionResults();
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        Ray ray = new Ray(click3d, dir);

        rootNode.collideWith(ray, results);
        Voxel voxelAt = null;
        if (results.size() > 0) {
            Vector3f contactPoint = results.getCollision(0).getContactPoint();
            Vector3f contactNormal = results.getCollision(0).getContactNormal();
            float x = contactPoint.x;
            float y = contactPoint.y;
            float z = contactPoint.z;
            if (contactNormal.x == 1.0f) {
                x -= 1f;
            }
            if (contactNormal.y == 1.0f) {
                y -= 1f;
            }
            if (contactNormal.z == 1.0f) {
                z -= 1f;
            }

            voxelAt = worldManager.getWorld().getVoxelAt(x, y, z);
        }
        return voxelAt;
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MOUSE_MOTION)) {
            Voxel voxelAt = getVoxelAt();
            if (voxelAt != null) {
                hoverBoxes.detachAllChildren();
                Spatial clone = hoverSpatial.clone();
                clone.setLocalTranslation(clone.getLocalTranslation().x + voxelAt.getX(), clone.getLocalTranslation().y + voxelAt.getY(), clone.getLocalTranslation().z + voxelAt.getZ());
                hoverBoxes.attachChild(clone);
            }
        }
    }
}
