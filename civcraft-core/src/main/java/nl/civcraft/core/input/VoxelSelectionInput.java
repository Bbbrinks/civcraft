package nl.civcraft.core.input;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
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
public class VoxelSelectionInput extends AbstractAppState implements ActionListener {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private Node rootNode;

    @Autowired
    private WorldManager worldManager;

    private static final String SELECT_VOXEL = "SELECT_VOXEL";
    private Camera cam;
    private InputManager inputManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        registerInput(app.getInputManager());
        this.cam = app.getCamera();
        this.inputManager = app.getInputManager();
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(SELECT_VOXEL, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, SELECT_VOXEL);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(SELECT_VOXEL)) {
            CollisionResults results = new CollisionResults();
            Vector2f click2d = inputManager.getCursorPosition();
            Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
            Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
            Ray ray = new Ray(click3d, dir);

            rootNode.collideWith(ray, results);

            if(results.size() > 0) {
                Vector3f contactPoint = results.getCollision(0).getContactPoint();
                Vector3f contactNormal = results.getCollision(0).getContactNormal();
                float x = contactPoint.x;
                float y = contactPoint.y;
                float z = contactPoint.z;
                if(contactNormal.x == 1.0f){
                    x -= 1f;
                }
                if(contactNormal.y == 1.0f){
                    y -= 1f;
                }
                if(contactNormal.z == 1.0f){
                    z -= 1f;
                }

                Voxel voxelAt = worldManager.getWorld().getVoxelAt(x, y, z);
                if (voxelAt != null) {
                    LOGGER.warn("Clicked on " + voxelAt.getType() + " @(" + x + ", " + y + ", " + z + " )");
                }
            }
        }
    }

}
