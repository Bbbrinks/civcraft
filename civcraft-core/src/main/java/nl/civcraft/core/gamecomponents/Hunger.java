package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.worldgeneration.Apple;

import java.util.Optional;


public class Hunger extends AbstractGameComponent {
    private Civvy civvy;
    private int calories;
    private int starvation;

    @Override
    public void addTo(GameObject gameObject) {
        Optional<Civvy> component = gameObject.getComponent(Civvy.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Hunger can only be added to Civvies");
        }
        this.civvy = component.get();
        calories = 3000;
    }

    public void handleTick() {
        if (calories > 1) {
            calories -= 1;
        } else {
            starvation += 1;
        }
        if (calories > 200 && starvation > 0) {
            starvation -= 10;
        }
        if (starvation > 200) {
            civvy.kill();
        }
    }

    public void eat(Apple apple) {
        calories += apple.getCalories();
    }
}
