package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Harvest;
import nl.civcraft.core.tasks.MoveTo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 20-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelSelectionInput extends AbstractAppState implements AnalogListener,ActionListener {

    private static final String MOUSE_MOTION = "MOUSE_MOTION";
    private static final String DELETE_VOXEL = "DELETE_VOXEL";
    private static final String SELECT_VOXEL = "SELECT_VOXEL";
    private static final String MOVE_TO = "MOVE_TO";
    private static final String HARVEST = "HARVEST";
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private Node rootNode;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private WorldManager worldManager;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private Spatial selectionSpatial;
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private Spatial hoverSpatial;
    private Camera cam;
    private InputManager inputManager;
    private Node selectionBoxes;
    private Node hoverBoxes;
    private Voxel currentVoxel;
    @Autowired
    private TaskManager taskManger;
    @Autowired
    private AStarPathFinder pathFinder;

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
        inputManager.addMapping(MOVE_TO, new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(HARVEST, new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(this, SELECT_VOXEL, MOUSE_MOTION, DELETE_VOXEL, MOVE_TO, HARVEST);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (isPressed) {
            if (currentVoxel != null) {
                if(name.equals(SELECT_VOXEL)) {
                    currentVoxel.isVisible();
                    currentVoxel.getNeighbour(Face.TOP);
                    selectionBoxes.detachAllChildren();
                    Spatial clone = selectionSpatial.clone();
                    clone.setLocalTranslation(clone.getLocalTranslation().x + currentVoxel.getX(), clone.getLocalTranslation().y + currentVoxel.getY(), clone.getLocalTranslation().z + currentVoxel.getZ());
                    selectionBoxes.attachChild(clone);
                }
                if(name.equals(DELETE_VOXEL)){
                    selectionBoxes.detachAllChildren();
                    hoverBoxes.detachAllChildren();
                    currentVoxel.breakBlock();
                }
                if (name.equals(MOVE_TO)) {
                    taskManger.addTask(new MoveTo(currentVoxel, pathFinder));
                }
                if (name.equals(HARVEST)) {
                    taskManger.addTask(new Harvest(currentVoxel, pathFinder));
                }
            }
        }


    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals(MOUSE_MOTION)) {
            Voxel voxelAt = getVoxelAt();
            if (voxelAt != null) {
                currentVoxel = voxelAt;
                hoverBoxes.detachAllChildren();
                Spatial clone = hoverSpatial.clone();
                clone.setLocalTranslation(clone.getLocalTranslation().x + currentVoxel.getX(), clone.getLocalTranslation().y + currentVoxel.getY(), clone.getLocalTranslation().z + currentVoxel.getZ());
                hoverBoxes.attachChild(clone);
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
            int x = Math.round(contactPoint.x);
            int y = Math.round(contactPoint.y);
            int z = Math.round(contactPoint.z);


            voxelAt = worldManager.getWorld().getVoxelAt(x, y, z);
        }
        return voxelAt;
    }
}
