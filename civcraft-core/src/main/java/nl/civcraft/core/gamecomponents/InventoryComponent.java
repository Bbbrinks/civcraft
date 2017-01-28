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
        return inventory.addItem(item);
    }

    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    public void remove(GameObject item) {
        inventory.remove(item);
    }

    public boolean hasRoom(GameObject item) {
        return inventory.hasRoom(item);
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        inventory.setLocation(gameObject.getTransform().getTranslation());
    }

    @Override
    public void changed() {
        super.changed();
        inventory.setLocation(gameObject.getTransform().getTranslation());
    }

    public static class Factory implements GameComponentFactory<InventoryComponent> {
        private final int size;

        public Factory(int size) {
            this.size = size;
        }

        @Override
        public InventoryComponent build() {
            return new InventoryComponent(new LimitedInventory(size, Vector3f.UNIT_XYZ));
        }

        @Override
        public Class<InventoryComponent> getComponentType() {
            return InventoryComponent.class;
        }


    }
}
