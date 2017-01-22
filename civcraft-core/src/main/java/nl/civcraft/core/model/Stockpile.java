package nl.civcraft.core.model;

import java.util.*;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Stockpile {
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
        item.getTransform().setTranslation(voxels.stream().findFirst().get().getTransform().getTranslation().clone());
        items.add(item);
        return true;
    }
}
