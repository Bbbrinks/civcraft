package nl.civcraft.core.gamecomponents;


import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.LimitedInventory;
import org.joml.Vector3f;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class InventoryComponent extends AbstractGameComponent implements GameComponent {
    private final Inventory inventory;

    private InventoryComponent(Inventory inventory) {
        this.inventory = inventory;
    }


    public Optional<GameObject> getFirstItem() {
        return inventory.getFirstItem();
    }

    public boolean addItem(GameObject item) {
        item.getTransform().setTranslation(getGameObject().getTransform().getTranslation(new Vector3f()));
        return inventory.addItem(item);
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public void remove(GameObject item) {
        item.getTransform().setTranslation(getGameObject().getTransform().getTranslation(new Vector3f()).add(new Vector3f(0, 1, 0)));
        inventory.remove(item);
    }

    public Optional<GameObject> removeItem(String itemType) {
        return inventory.removeItem(itemType).map(gameObject1 -> {
            gameObject1.getTransform().setTranslation(getGameObject().getTransform().getTranslation(new Vector3f()).add(new Vector3f(0, 1, 0)));
            return gameObject1;
        });
    }

    public boolean hasRoom(GameObject item) {
        return inventory.hasRoom(item);
    }


    public static class Factory implements GameComponentFactory<InventoryComponent> {
        private final int size;

        public Factory(int size) {
            this.size = size;
        }

        @Override
        public InventoryComponent build() {
            return new InventoryComponent(new LimitedInventory(size));
        }

        @Override
        public Class<InventoryComponent> getComponentType() {
            return InventoryComponent.class;
        }


    }
}
