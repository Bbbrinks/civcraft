package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class LimitedInventory implements Inventory, GameComponent {
    private final Item[] items;
    private GameObject gameObject;

    public LimitedInventory(int size) {
        items = new Item[size];
    }

    @Override
    public void addTo(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void destroyed(GameObject gameObject) {

    }

    @Override
    public Optional<Item> getFirstItem() {
        Item firstItem = items[0];
        items[0] = items[1];
        if (gameObject != null) {
            gameObject.changed();
        }
        return Optional.ofNullable(firstItem);
    }

    @Override
    public boolean addItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            Item slotItem = items[i];
            if (slotItem == null) {
                items[i] = item;
                if (gameObject != null) {
                    gameObject.changed();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void remove(Item item) {
        //TODO remove item
    }
}
