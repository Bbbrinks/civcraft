package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class LimitedInventory extends AbstractGameComponent implements Inventory, GameComponent {
    private final GameObject[] items;

    public LimitedInventory(int size) {
        items = new GameObject[size];
    }


    @Override
    public Optional<GameObject> getFirstItem() {
        GameObject firstItem = items[0];
        items[0] = items[1];
        if (gameObject != null) {
            gameObject.changed();
        }
        return Optional.ofNullable(firstItem);
    }

    @Override
    public boolean addItem(GameObject item) {
        for (int i = 0; i < items.length; i++) {
            GameObject slotItem = items[i];
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
    public void remove(GameObject item) {
        //TODO remove item
    }

    public static class Factory implements GameComponentFactory<LimitedInventory> {
        private final int size;

        public Factory(int size) {
            this.size = size;
        }

        @Override
        public LimitedInventory build() {
            return new LimitedInventory(size);
        }

        @Override
        public Class<LimitedInventory> getComponentType() {
            return LimitedInventory.class;
        }


    }
}
