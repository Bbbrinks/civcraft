package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.Task;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class HarvestFromInventory implements Harvestable, GameComponent {
    private GameObject gameObject;

    @Override
    public Task.Result harvest(Civvy civvy) {
        Optional<Inventory> component = gameObject.getComponent(Inventory.class);
        Item item = component.get().getFirstItem();
        if (item != null) {
            boolean addedItem = civvy.getComponent(Inventory.class).get().addItem(item);
            return addedItem ? Task.Result.COMPLETED : Task.Result.FAILED;
        }
        return Task.Result.FAILED;
    }

    @Override
    public void addTo(GameObject gameObject) {
        Optional<Inventory> component = gameObject.getComponent(Inventory.class);
        if (!component.isPresent()) {
            throw new RuntimeException("HarvestFromInventory can only be added to GameObjects with an Inventory component");
        }
        this.gameObject = gameObject;
    }
}
