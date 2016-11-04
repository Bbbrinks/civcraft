package nl.civcraft.jme3.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.scene.Node;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoxelSelectionInput implements AnalogListener, ActionListener {

    private static final String MOUSE_MOTION = "MOUSE_MOTION";
    private static final String DELETE_VOXEL = "DELETE_VOXEL";
    private static final String SELECT_VOXEL = "SELECT_VOXEL";
    private static final String MOVE_TO = "MOVE_TO";
    private static final String HARVEST = "HARVEST";


    private Node selectionBoxes;
    private MouseTool currentTool;

    @Autowired
    public VoxelSelectionInput(Node rootNode, Node selectionBoxes, SingleVoxelSelector singleVoxelSelector, InputManager inputManager) {

        registerInput(inputManager);
        this.selectionBoxes = selectionBoxes;
        currentTool = singleVoxelSelector;

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
        if (currentTool != null) {
            if (name.equals(SELECT_VOXEL)) {
                currentTool.handleLeftClick(isPressed);
            }
        }

       /* if (isPressed) {
            if (currentVoxel != null) {
                if(name.equals(SELECT_VOXEL)) {
                    currentTool.handleLeftClick(isPressed);
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
        }*/


    }

    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (currentTool != null) {
            currentTool.handleMouseMotion();
        }
    }

    public MouseTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(MouseTool currentTool) {
        this.currentTool = currentTool;
    }
}
