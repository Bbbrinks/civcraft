package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ItemComponent extends AbstractGameComponent {
    private GameObject item;

    public ItemComponent(GameObject item) {
        this.item = item;
    }

    public GameObject getItem() {
        return item;
    }

    public void setItem(GameObject item) {
        this.item = item;
    }

    public static class Factory implements GameComponentFactory<ItemComponent> {
        @Override
        public ItemComponent build() {
            return new ItemComponent(null);
        }

        @Override
        public Class<ItemComponent> getComponentType() {
            return ItemComponent.class;
        }
    }
}
