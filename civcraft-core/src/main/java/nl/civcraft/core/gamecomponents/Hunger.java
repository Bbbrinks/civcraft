package nl.civcraft.core.gamecomponents;

import io.reactivex.disposables.Disposable;
import nl.civcraft.core.managers.TickManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;


public class Hunger extends AbstractGameComponent {
    private final Disposable subscribe;
    private int calories;
    private int starvation;

    @Autowired
    public Hunger(TickManager tickManager) {
        subscribe = tickManager.getTick().subscribe(this::handleTick);
    }

    @Override
    public void addTo(GameObject gameObject) {
        Optional<Civvy> component = gameObject.getComponent(Civvy.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Hunger can only be added to Civvies");
        }
        calories = 3000;
    }

    @Override
    public void destroyed() {
        subscribe.dispose();
        super.destroyed();
    }

    private void handleTick(Long tpt) {
        if (calories > 1) {
            calories -= 1;
        } else {
            starvation += 1;
        }
        if (calories > 200 && starvation > 0) {
            starvation -= 10;
        }
        if (starvation > 200) {
            gameObject.destroy();
        }
    }

    public static class Factory implements GameComponentFactory<Hunger> {
        private final TickManager tickManager;

        @Autowired
        public Factory(TickManager tickManager) {
            this.tickManager = tickManager;
        }

        @Override
        public Hunger build() {
            return new Hunger(tickManager);
        }

        @Override
        public Class<Hunger> getComponentType() {
            return Hunger.class;
        }
    }


}
