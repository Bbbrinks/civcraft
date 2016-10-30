package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.Physical;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Haul;
import nl.civcraft.core.tasks.Task;

import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Haulable implements GameComponent {
    private GameObject gameObject;

    @Override
    public void addTo(GameObject gameObject) {
        Optional<ItemComponent> component = gameObject.getComponent(ItemComponent.class);
        Optional<Physical> component1 = gameObject.getComponent(Physical.class);
        if (!component.isPresent() || !component1.isPresent()) {
            throw new IllegalStateException("Can only be applied to ItemComponent and Physical");
        }
        this.gameObject = gameObject;
    }

    @Override
    public void destroyed(GameObject gameObject) {
        //No op
    }

    public Task getTask(Stockpile target, AStarPathFinder pathFinder) {
        return new Haul(target, gameObject, pathFinder);
    }
}
