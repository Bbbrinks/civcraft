package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.tasks.Haul;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Haulable extends AbstractGameComponent {

    private Haul task;

    public Task getTask(Stockpile target) {
        this.task = new Haul(target, gameObject);
        return task;
    }

    public boolean shouldBeHauled() {
        boolean isInInventory = gameObject.getComponent(ItemComponent.class).map(ItemComponent::isInInventory).orElse(false);

        if (task != null && task.getState().equals(Task.State.DONE)) {
            task = null;
        }

        return task == null && !isInInventory;
    }

    public static class Factory implements GameComponentFactory<Haulable> {
        @Override
        public Haulable build() {
            return new Haulable();
        }

        @Override
        public Class<Haulable> getComponentType() {
            return Haulable.class;
        }
    }
}
