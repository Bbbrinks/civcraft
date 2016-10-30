package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.worldgeneration.Apple;


public class Hunger implements GameComponent {
    private Civvy civvy;
    private int calories;
    private int starvation;

    @Override
    public void addTo(GameObject gameObject) {
        if (!(gameObject instanceof Civvy)) {
            throw new IllegalStateException("Hunger can only be added to Civvies");
        }
        this.civvy = (Civvy) gameObject;
        calories = 3000;
    }

    @Override
    public GameObject getGameObject() {
        return civvy;
    }

    @Override
    public void destroyed(GameObject gameObject) {
        //No op
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
