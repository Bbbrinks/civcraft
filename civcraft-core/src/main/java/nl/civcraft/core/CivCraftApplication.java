package nl.civcraft.core;

import com.jme3.input.InputManager;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.JmeContext;
import nl.civcraft.core.event.PostRender;
import nl.civcraft.core.event.PreRender;
import nl.civcraft.core.event.RequestClose;
import nl.civcraft.core.event.SystemUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CivCraftApplication {


    private final Node rootNode;
    private final Node guiNode;
    private final JmeContext context;
    private final ViewPort mainViewPort;
    private final ViewPort guiViewPort;

    private final InputManager inputManager;
    private final RenderManager renderManager;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public CivCraftApplication(Node rootNode, Node guiNode, JmeContext context, ViewPort mainViewPort, ViewPort guiViewPort, InputManager inputManager, RenderManager renderManager, ApplicationEventPublisher publisher) {
        super();
        this.rootNode = rootNode;
        this.guiNode = guiNode;
        this.context = context;
        this.mainViewPort = mainViewPort;
        this.guiViewPort = guiViewPort;
        this.inputManager = inputManager;
        this.renderManager = renderManager;
        this.publisher = publisher;
        guiNode.setQueueBucket(RenderQueue.Bucket.Gui);
        guiNode.setCullHint(Spatial.CullHint.Never);
        mainViewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);
    }


    @EventListener
    public void update(SystemUpdate systemUpdate) {

        inputManager.update(systemUpdate.getTpf());


        rootNode.updateLogicalState(systemUpdate.getTpf());
        guiNode.updateLogicalState(systemUpdate.getTpf());

        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        // render states
        publisher.publishEvent(new PreRender(this));
        renderManager.render(systemUpdate.getTpf(), context.isRenderable());
        publisher.publishEvent(new PostRender(this));
    }

    @EventListener
    public void requestClose(RequestClose requestClose) {
        context.destroy(false);
        System.exit(0);
    }
}
