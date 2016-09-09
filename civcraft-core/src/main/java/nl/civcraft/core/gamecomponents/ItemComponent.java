package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ItemComponent implements GameComponent {
    private final Item item;

    public ItemComponent(Item item) {
        this.item = item;
    }

    @Override
    public void addTo(GameObject gameObject) {

    }

    @Override
    public void destroyed(GameObject gameObject) {

    }

    public Item getItem() {
        return item;
    }
}
