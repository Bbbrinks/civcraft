package nl.civcraft.core;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftApplication extends Application {

    @Autowired
    private Node rootNode;

    @Autowired
    private Node guiNode;

    public CivCraftApplication( ) {
        super();

    }

    public void addAppStates(AppState... states){
        if (states != null) {
            for (AppState a : states) {
                if (a != null) {
                    stateManager.attach(a);
                }
            }
        }
    }

    @Override
    public void start() {
        setSettings(new AppSettings(true));
        super.start();
    }

    @Override
    public void initialize() {
        super.initialize();

        guiNode.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.setCullHint(Spatial.CullHint.Never);
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);

    }

    @Override
    public void update() {
        super.update(); // makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;

        // update states
        stateManager.update(tpf);

        rootNode.updateLogicalState(tpf);
        guiNode.updateLogicalState(tpf);

        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        // render states
        stateManager.render(renderManager);
        renderManager.render(tpf, context.isRenderable());
        stateManager.postRender();
    }
}
