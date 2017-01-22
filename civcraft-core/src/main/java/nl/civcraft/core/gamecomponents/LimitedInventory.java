package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class LimitedInventory extends AbstractGameComponent implements Inventory, GameComponent {
    private final GameObject[] items;

    private LimitedInventory(int size) {
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
        item.getTransform().setTranslation(getGameObject().getTransform().getTranslation().clone());
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
