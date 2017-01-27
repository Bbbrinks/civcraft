package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Stockpile extends AbstractGameComponent implements Serializable {
    private final Set<GameObject> voxels;
    private final List<GameObject> items;

    public Stockpile() {
        this.items = new ArrayList<>();
        voxels = new HashSet<>();
    }

    public void addVoxel(GameObject voxelAt) {
        this.voxels.add(voxelAt);
    }

    public Set<GameObject> getVoxels() {
        return voxels;
    }

    public Optional<GameObject> getAvailableSpot(GameObject item) {
        return voxels.stream().findFirst();
    }

    @SuppressWarnings({"UnusedReturnValue", "SameReturnValue"})
    public boolean addItem(GameObject item) {
        item.getTransform().setTranslation(voxels.stream().
                findFirst().
                orElseThrow(() -> new IllegalStateException("Stockpile no voxels")).
                getTransform().getTranslation().clone());
        item.getComponent(ItemComponent.class).ifPresent(i -> i.setInInventory(true));
        items.add(item);
        return true;
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
