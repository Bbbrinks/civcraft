package nl.civcraft.core.model;

import java.util.*;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Stockpile {
    private final Set<Voxel> voxels;
    private final List<Item> items;

    public Stockpile() {
        this.items = new ArrayList<>();
        voxels = new HashSet<>();
    }

    public void addVoxel(Voxel voxelAt) {
        this.voxels.add(voxelAt);
    }

    public Set<Voxel> getVoxels() {
        return voxels;
    }

    public Optional<Voxel> getAvailableSpot(Item item) {
        return voxels.stream().findFirst();
    }

    public boolean addItem(Item item) {
        items.add(item);
        return true;
    }
}
