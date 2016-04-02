package nl.civcraft.core;

import com.jme3.system.SystemListener;
import com.jme3.system.Timer;
import nl.civcraft.core.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SystemEventHandler implements SystemListener {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ApplicationEventPublisher publisher;
    private final int speed = 1;
    private Timer timer;
    private boolean initialized = false;

    @Autowired
    public SystemEventHandler(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void initialize() {
        initialized = true;
    }

    @Override
    public void reshape(int width, int height) {
        publisher.publishEvent(new ReshapeWindow(this));
    }

    @Override
    public void update() {
        if (!initialized) {
            return;
        }
        if (timer == null) {
            return;
        }
        if (speed == 0)
            return;

        timer.update();
        float tpf = timer.getTimePerFrame() * speed;
        publisher.publishEvent(new SystemUpdate(tpf, this));
    }

    @Override
    public void requestClose(boolean esc) {
        publisher.publishEvent(new RequestClose(this));
    }

    @Override
    public void gainFocus() {
        publisher.publishEvent(new GainFocus(this));
    }

    @Override
    public void loseFocus() {
        publisher.publishEvent(new LoseFocus(this));
    }

    @Override
    public void handleError(String errorMsg, Throwable t) {
        LOGGER.error(errorMsg, t);
    }

    @Override
    public void destroy() {
        publisher.publishEvent(new ApplicationDestroy(this));
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
