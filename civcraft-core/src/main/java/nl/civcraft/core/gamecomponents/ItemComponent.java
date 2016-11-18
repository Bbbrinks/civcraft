package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Item;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ItemComponent extends AbstractGameComponent {
    private Item item;

    public ItemComponent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public static class Factory implements GameComponentFactory<ItemComponent> {
        @Override
        public GameComponent build() {
            return new ItemComponent(null);
        }

        @Override
        public Class<ItemComponent> getComponentType() {
            return ItemComponent.class;
        }
    }
}
