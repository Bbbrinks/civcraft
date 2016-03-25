package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Item;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Inventory {
    Item getFirstItem();

    boolean addItem(Item item);
}
