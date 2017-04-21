package nl.civcraft.jme3.input;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import nl.civcraft.core.interaction.MouseTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class VoxelSelectionInput implements AnalogListener, ActionListener {

    private static final String MOUSE_MOTION_X = "MOUSE_MOTION_X";
    private static final String MOUSE_MOTION_Y = "MOUSE_MOTION_Y";
    private static final String MOUSE_MOTION_X_NEG = "MOUSE_MOTION_X_NEG";
    private static final String MOUSE_MOTION_Y_NEG = "MOUSE_MOTION_Y_NEG";
    private static final String DELETE_VOXEL = "DELETE_VOXEL";
    private static final String SELECT_VOXEL = "SELECT_VOXEL";
    private static final String MOVE_TO = "MOVE_TO";
    private static final String HARVEST = "HARVEST";
    private static final String UNSELECT_TOOL = "UNSELECT_TOOL";
    private final MouseTool defaultTool;


    private MouseTool currentTool;

    @Autowired
    public VoxelSelectionInput(@Qualifier("singleVoxelSelector") MouseTool defaultTool,
                               InputManager inputManager) {
        registerInput(inputManager);
        currentTool = defaultTool;
        this.defaultTool = defaultTool;
    }

    private void registerInput(InputManager inputManager) {
        inputManager.addMapping(SELECT_VOXEL, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(DELETE_VOXEL, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addMapping(UNSELECT_TOOL, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping(MOUSE_MOTION_X, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(MOUSE_MOTION_Y, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(MOUSE_MOTION_X_NEG, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(MOUSE_MOTION_Y_NEG, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(MOVE_TO, new KeyTrigger(KeyInput.KEY_M));
        inputManager.addMapping(HARVEST, new KeyTrigger(KeyInput.KEY_H));
        inputManager.addListener(this, SELECT_VOXEL, MOUSE_MOTION_Y, MOUSE_MOTION_X, MOUSE_MOTION_X_NEG, MOUSE_MOTION_Y_NEG, DELETE_VOXEL, MOVE_TO, HARVEST);
    }


    @Override
    public void onAction(String name,
                         boolean isPressed,
                         float tpf) {

        if (isPressed) {
            if (currentTool != null) {
                if (name.equals(SELECT_VOXEL)) {
                    currentTool.handleLeftClick(isPressed);
                }
            }
            if (name.equals(UNSELECT_TOOL)) {
                currentTool = defaultTool;
            }
        }
    }

    @Override
    public void onAnalog(String name,
                         float value,
                         float tpf) {
        if (currentTool != null) {
            if (name.equals(MOUSE_MOTION_X)) {
                currentTool.handleMouseMotion(value, 0.0f);
            }
            if (name.equals(MOUSE_MOTION_Y)) {
                currentTool.handleMouseMotion(0.0f, value);
            }
            if (name.equals(MOUSE_MOTION_X_NEG)) {
                currentTool.handleMouseMotion(-1f * value, 0.0f);
            }
            if (name.equals(MOUSE_MOTION_Y_NEG)) {
                currentTool.handleMouseMotion(0.0f, -1f * value);
            }
        }
    }

    public MouseTool getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(MouseTool currentTool) {
        this.currentTool = currentTool;
    }
}
