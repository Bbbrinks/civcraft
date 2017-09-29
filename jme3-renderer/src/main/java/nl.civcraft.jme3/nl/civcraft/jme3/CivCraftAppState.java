package nl.civcraft.jme3.nl.civcraft.jme3;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.renderer.RenderManager;
import io.reactivex.subjects.Subject;
import nl.civcraft.core.SystemEventPublisher;
import nl.civcraft.jme3.nl.civcraft.jme3.modules.CivCraftJme3Module;

/**
 * Created by Bob on 12-8-2016.
 * <p>
 * This is probably not worth documenting
 */
class CivCraftAppState implements AppState {
    private Subject<Float> eventPublisher;
    private boolean initialized;
    private boolean enabled;
    private Injector injector;

    @Override
    public void initialize(AppStateManager appStateManager, Application application) {
        injector = Guice.createInjector(new CivCraftJme3Module());
        eventPublisher = injector.getInstance(SystemEventPublisher.class).getPublisher();
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
        //NoOp
    }

    @Override
    public void stateDetached(AppStateManager appStateManager) {
        //NoOp
    }

    @Override
    public void update(float tpf) {
        eventPublisher.onNext(tpf);
    }

    @Override
    public void render(RenderManager renderManager) {
        //NoOp
    }

    @Override
    public void postRender() {
        //NoOp
    }

    @Override
    public void cleanup() {
    }
}