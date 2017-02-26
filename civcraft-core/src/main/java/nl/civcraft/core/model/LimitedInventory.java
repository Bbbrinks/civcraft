package nl.civcraft.core.model;

import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.gamecomponents.ItemComponent;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class LimitedInventory implements Inventory {
    private final GameObject[] items;

    public LimitedInventory(int size) {
        items = new GameObject[size];
    }

    @Override
    public Optional<GameObject> getFirstItem() {
        for (GameObject item : items) {
            if (item != null) {
                remove(item);
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean addItem(GameObject item) {
        ItemComponent itemComponent = item.getComponent(ItemComponent.class).map(i -> i).orElseThrow(() -> new IllegalStateException("Not an item"));
        for (int i = 0; i < items.length; i++) {
            GameObject slotItem = items[i];
            if (slotItem == null) {
                items[i] = item;
                itemComponent.setInInventory(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        for (GameObject item : items) {
            if (item != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void remove(GameObject item) {
        for (int i = 0; i < items.length; i++) {
            GameObject object = items[i];
            if (Objects.equals(object, item)) {
                item.getComponent(ItemComponent.class).ifPresent(foundItem -> foundItem.setInInventory(false));
                items[i] = null;
            }
        }
    }

    @Override
    public boolean hasRoom(GameObject item) {
        for (GameObject spot : items) {
            if (spot == null) {
                return true;
            }
        }
        return false;
    }
}
