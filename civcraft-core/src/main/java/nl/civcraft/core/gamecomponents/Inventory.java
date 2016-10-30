package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Item;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Inventory extends GameComponent {
    Optional<Item> getFirstItem();

    boolean addItem(Item item);

    boolean isEmpty();

    void remove(Item item);
}
