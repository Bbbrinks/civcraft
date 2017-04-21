package nl.civcraft.core.gamecomponents;

import com.jme3.math.Vector3f;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.LimitedInventory;

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
        item.getTransform().setTranslation(getGameObject().getTransform().getTranslation().clone());
        return inventory.addItem(item);
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public void remove(GameObject item) {
        item.getTransform().setTranslation(getGameObject().getTransform().getTranslation().add(Vector3f.UNIT_Y));
        inventory.remove(item);
    }

    public Optional<GameObject> removeItem(String itemType) {
        return inventory.removeItem(itemType).map(gameObject1 -> {
            gameObject1.getTransform().setTranslation(getGameObject().getTransform().getTranslation().add(Vector3f.UNIT_Y));
            return gameObject1;
        });
    }

    public boolean hasRoom(GameObject item) {
        return inventory.hasRoom(item);
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
    }

    @Override
    public void changed() {
        super.changed();
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
