package nl.civcraft.core.debug;


import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO: after intellij 16 release refactor jmonkey engine to allow renderer to be injected.
@Service
public class DebugStatsState extends AbstractAppState implements ActionListener {

    protected static final String TOGGLE_DEBUG_INFO = "TOGGLE_DEBUG_INFO";
    public static String LAST_MESSAGE; //NOSONAR
    private final Node guiNode;
    private final BitmapText fpsText;
    private final BitmapText logMessageText;
    private Application app;
    private float secondCounter;
    private int frameCounter;
    private boolean show = false;
    private Node debugNode;

    @Autowired
    public DebugStatsState(Node guiNode, BitmapText fpsText, BitmapText logMessageText) {
        this.guiNode = guiNode;
        this.fpsText = fpsText;
        this.logMessageText = logMessageText;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        debugNode = new Node("debugNode");
        guiNode.attachChild(debugNode);

        loadFpsText();
        loadLogMessageText();
        registerInputs(app.getInputManager());

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

    @Override
    public void update(float tpf) {
        if (!show) {
            debugNode.detachAllChildren();
        } else {
            debugNode.attachChild(fpsText);
            debugNode.attachChild(logMessageText);
        }

        secondCounter += app.getTimer().getTimePerFrame();
        frameCounter++;
        if (secondCounter >= 1.0f) {
            int fps = (int) (frameCounter / secondCounter);
            fpsText.setText("Frames per second: " + fps);
            secondCounter = 0.0f;
            frameCounter = 0;
        }
        logMessageText.setText(LAST_MESSAGE);

    }

    @Override
    public void cleanup() {
        super.cleanup();
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
