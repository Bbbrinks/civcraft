package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.events.CivvyCreated;
import nl.civcraft.core.model.events.CivvyRemoved;
import nl.civcraft.core.model.events.Tick;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class HungerBinder {

    private final List<Hunger> hungers;
    private final List<Hunger> removedHungers;

    public HungerBinder() {
        hungers = new ArrayList<>();
        removedHungers = new ArrayList<>();
    }

    @EventListener
    public void handleCivvyCreated(CivvyCreated civvyCreated) {
        Hunger hunger = new Hunger();
        civvyCreated.getCivvy().addComponent(hunger);
        hungers.add(hunger);
    }

    @EventListener
    public void handleCivvyRemoved(CivvyRemoved civvyRemoved) {
        Optional<Hunger> component = civvyRemoved.getCivvy().getComponent(Hunger.class);
        if (component.isPresent()) {
            removedHungers.add(component.get());
        }
    }

    @EventListener
    public void handleTick(Tick tick) {
        hungers.removeAll(removedHungers);
        removedHungers.clear();
        for (Hunger hunger : hungers) {
            hunger.handleTick();
        }


    }
}
