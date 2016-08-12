package nl.civcraft.core;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import nl.civcraft.core.conf.InitialConfiguration;
import nl.civcraft.core.event.SystemUpdate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftAppState implements AppState {
    public ApplicationEventPublisher applicationEventPublisher;
    private boolean initialized;
    private boolean enabled;

    @Override
    public void initialize(AppStateManager appStateManager, Application application) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(InitialConfiguration.class);
        applicationEventPublisher = appContext.getBean(SystemEventHandler.class).getPublisher();
        initialized = true;
        enabled = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setEnabled(boolean b) {
        enabled = b;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager appStateManager) {

    }

    @Override
    public void stateDetached(AppStateManager appStateManager) {

    }

    @Override
    public void update(float tpf) {
        applicationEventPublisher.publishEvent(new SystemUpdate(tpf, this));
    }

    @Override
    public void render(RenderManager renderManager) {

    }

    @Override
    public void postRender() {

    }

    @Override
    public void cleanup() {

    }
}