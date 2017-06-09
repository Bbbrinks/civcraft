package nl.civcraft.core.gamecomponents;

import com.jme3.math.Vector3f;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.LimitedInventory;

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

    public Stockpile() {
        voxels = new HashMap<>();
    }

    public void addVoxel(GameObject voxelAt) {
        this.voxels.put(voxelAt, new LimitedInventory(5));
    }

    public Set<GameObject> getVoxels() {
        return voxels.keySet();
    }

    public boolean addItem(GameObject item) {
        return getAvailableSpot(item).
                map(spot -> {
                    if (voxels.get(spot).addItem(item)) {
                        item.getTransform().setTranslation(spot.getTransform().getTranslation().add(Vector3f.UNIT_Y));
                        return true;
                    }
                    return false;
                }).
                orElse(false);
    }

    public Optional<GameObject> getAvailableSpot(GameObject item) {
        return voxels.entrySet().stream().
                filter(e -> e.getValue().hasRoom(item)).
                map(Map.Entry::getKey).
                findFirst();
    }

    public boolean hasItem(String itemComponent) {
        return voxels.values().stream().anyMatch(inventory -> inventory.hasItem(itemComponent));
    }

    public Optional<GameObject> getSpotWithItem(String itemComponent) {
        return voxels.entrySet().stream()
                .filter(gameObjectInventoryEntry -> gameObjectInventoryEntry.getValue().hasItem(itemComponent))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public Optional<GameObject> removeItem(String itemComponent) {
        Optional<Inventory> first = voxels.values().stream()
                .filter(inventory -> inventory.hasItem(itemComponent))
                .findFirst();
        return first.flatMap(inventory -> inventory.removeItem(itemComponent));
    }

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
