package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.LimitedInventory;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Stockpile extends AbstractGameComponent implements Serializable {
    private final Map<GameObject, Inventory> voxels;
    private final InventoryComponent.Factory inventoryFactory;

    public Stockpile() {
        voxels = new HashMap<>();
        inventoryFactory = new InventoryComponent.Factory(5);
    }

    public void addVoxel(GameObject voxelAt) {
        this.voxels.put(voxelAt, new LimitedInventory(5, voxelAt.getTransform().getTranslation().add(0, 1, 0)));
    }

    public Set<GameObject> getVoxels() {
        return voxels.keySet();
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public boolean addItem(GameObject item) {
        return getAvailableSpot(item).
                map(spot -> voxels.get(spot).addItem(item)).
                orElse(false);
    }

    public Optional<GameObject> getAvailableSpot(GameObject item) {
        return voxels.entrySet().stream().
                filter(e -> e.getValue().hasRoom(item)).
                map(Map.Entry::getKey).
                findFirst();
    }

    @Component
    public static class Factory implements GameComponentFactory<Stockpile> {

        @Override
        public Stockpile build() {
            return new Stockpile();
        }

        @Override
        public Class<Stockpile> getComponentType() {
            return Stockpile.class;
        }
    }
}
