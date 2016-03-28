package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.Tick;
import nl.civcraft.core.npc.Civvy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Hunger implements GameComponent {
    private static final Logger LOGGER = LogManager.getLogger();
    private Civvy civvy;
    private int calories;
    private int starvation;

    @Override
    public void addTo(GameObject gameObject) {
        if (!(gameObject instanceof Civvy)) {
            throw new RuntimeException("Hunger can only be added to Civvies");
        }
        this.civvy = (Civvy) gameObject;
        calories = 3000;
    }

    public void handleTick(Tick tick) {
        if (calories > 10) {
            calories -= 10;
        } else {
            starvation += 10;
        }
        if (calories > 200 && starvation > 0) {
            starvation -= 10;
        }
        if (starvation > 200) {
            civvy.kill();
        }
        LOGGER.warn("Hunger states " + calories + " " + starvation);
    }
}
