package nl.civcraft.core.debug;


import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugStatsState extends AbstractAppState implements ActionListener {

    private static final String TOGGLE_DEBUG_INFO = "TOGGLE_DEBUG_INFO";
    public static String LAST_MESSAGE; //NOSONAR
    private Application app;
    @Autowired
    private Node guiNode;
    private BitmapFont guiFont;
    private BitmapText fpsText;
    private StatsView statsView;
    private BitmapText logMessageText;
    private float secondCounter;
    private int frameCounter;
    private boolean show = false;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");


        loadFpsText();
        loadStatsView();
        loadLogMessageText();
        registerInputs(app.getInputManager());
    }

    /**
     * Attaches FPS statistics to guiNode and displays it on the screen.
     */
    private void loadFpsText() {
        if (fpsText == null) {
            fpsText = new BitmapText(guiFont, false);
        }

        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        fpsText.setCullHint(Spatial.CullHint.Never);
        guiNode.attachChild(fpsText);

    }

    /**
     * Attaches Statistics View to guiNode and displays it on the screen
     * above FPS statistics line.
     */
    private void loadStatsView() {
        statsView = new StatsView("Statistics View",
                app.getAssetManager(),
                app.getRenderer().getStatistics());
        // move it up so it appears above fps text
        statsView.setLocalTranslation(0, fpsText.getLineHeight() * 3, 0);
        statsView.setEnabled(true);
        statsView.setCullHint(Spatial.CullHint.Never);
        guiNode.attachChild(statsView);
    }

    private void loadLogMessageText() {
        if (logMessageText == null) {
            logMessageText = new BitmapText(guiFont, false);
        }

        logMessageText.setLocalTranslation(0, logMessageText.getLineHeight() * 2, 0);
        logMessageText.setText("");
        logMessageText.setCullHint(Spatial.CullHint.Never);
        guiNode.attachChild(logMessageText);
    }

    private void registerInputs(InputManager inputManager) {
        inputManager.addMapping(TOGGLE_DEBUG_INFO, new KeyTrigger(KeyInput.KEY_F10));
        inputManager.addListener(this, TOGGLE_DEBUG_INFO);
    }

    @Override
    public void update(float tpf) {
        if (!show) {
            guiNode.detachAllChildren();
        } else {
            guiNode.attachChild(statsView);
            guiNode.attachChild(fpsText);
            guiNode.attachChild(logMessageText);
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

        guiNode.detachChild(statsView);
        guiNode.detachChild(fpsText);
        guiNode.detachChild(logMessageText);
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
