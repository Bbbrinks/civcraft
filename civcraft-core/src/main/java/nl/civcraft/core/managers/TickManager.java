package nl.civcraft.core.managers;

import nl.civcraft.core.model.events.Tick;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TickManager {

    private final ApplicationEventPublisher publisher;

    @Autowired
    public TickManager(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Scheduled(fixedRate = 100)
    public void tick() {
        publisher.publishEvent(new Tick(this));
    }
}
