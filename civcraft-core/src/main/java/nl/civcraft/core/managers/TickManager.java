package nl.civcraft.core.managers;

import nl.civcraft.core.Civcraft;
import nl.civcraft.core.model.events.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TickManager {

    private final ApplicationEventPublisher publisher;
    private final Civcraft civcraft;

    @Autowired
    public TickManager(ApplicationEventPublisher publisher, Civcraft civcraft) {
        this.civcraft = civcraft;
        this.publisher = publisher;
    }

    @Scheduled(fixedRate = 100)
    public void tick() {
        if (!civcraft.isPaused()) {
            publisher.publishEvent(new Tick(this));
        }
    }
}
