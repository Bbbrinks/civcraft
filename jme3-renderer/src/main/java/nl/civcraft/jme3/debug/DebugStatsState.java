package nl.civcraft.jme3.debug;


import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import nl.civcraft.core.SystemEventPublisher;

import javax.inject.Inject;
import javax.inject.Named;

public class DebugStatsState implements ActionListener {

    private static final String TOGGLE_DEBUG_INFO = "TOGGLE_DEBUG_INFO";
    private static String LAST_MESSAGE; //NOSONAR

    private final BitmapText fpsText;
    private final BitmapText logMessageText;
    private final Node debugNode;
    private float secondCounter;
    private int frameCounter;
    private boolean show = false;

    @Inject
    public DebugStatsState(@Named("guiNode") Node guiNode,
                           @Named("fpsText") BitmapText fpsText,
                           @Named("logMessageText") BitmapText logMessageText,
                           InputManager inputManager,
                           SystemEventPublisher systemEventPublisher) {
        systemEventPublisher.getPublisher().subscribe(this::update);
        this.fpsText = fpsText;
        this.logMessageText = logMessageText;
        debugNode = new Node("debugNode");
        guiNode.attachChild(debugNode);
        loadFpsText();
        loadLogMessageText();
        registerInputs(inputManager);
    }


    /**
     * Attaches FPS statistics to debugNode and displays it on the screen.
     */
    private void loadFpsText() {
        debugNode.attachChild(fpsText);
    }



    private void loadLogMessageText() {
        debugNode.attachChild(logMessageText);
    }

    private void registerInputs(InputManager inputManager) {
        inputManager.addMapping(TOGGLE_DEBUG_INFO, new KeyTrigger(KeyInput.KEY_F10));
        inputManager.addListener(this, TOGGLE_DEBUG_INFO);
    }

    public void update(float tpf) {
        if (!show) {
            debugNode.detachAllChildren();
        } else {
            debugNode.attachChild(fpsText);
            debugNode.attachChild(logMessageText);
        }

        secondCounter += tpf;
        frameCounter++;
        if (secondCounter >= 1.0f) {
            int fps = (int) (frameCounter / secondCounter);
            fpsText.setText("Frames per second: " + fps);
            secondCounter = 0.0f;
            frameCounter = 0;
        }
        logMessageText.setText(LAST_MESSAGE);

    }

    public void cleanup() {
        debugNode.detachChild(fpsText);
        debugNode.detachChild(logMessageText);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        if (name.equals(TOGGLE_DEBUG_INFO)) {
            show = !show;
        }
    }
}
