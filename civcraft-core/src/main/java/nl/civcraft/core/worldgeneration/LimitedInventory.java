package nl.civcraft.core.worldgeneration;

import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class LimitedInventory implements Inventory, GameComponent {
    private final Item[] items;

    public LimitedInventory(int size) {
        items = new Item[size];
    }

    @Override
    public void addTo(GameObject gameObject) {
        //No stuff needed
    }

    @Override
    public Item getFirstItem() {
        Item firstItem = items[0];
        items[0] = items[1];
        return firstItem;
    }

    @Override
    public boolean addItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            Item slotItem = items[i];
            if (slotItem == null) {
                items[i] = item;
                return true;
            }
        }
        return false;
    }
}
