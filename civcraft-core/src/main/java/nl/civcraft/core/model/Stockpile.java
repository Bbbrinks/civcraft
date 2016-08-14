package nl.civcraft.core.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Stockpile {
    private final Set<Voxel> voxels;

    public Stockpile() {
        voxels = new HashSet<>();
    }

    public void addVoxel(Voxel voxelAt) {
        this.voxels.add(voxelAt);
    }

    public Set<Voxel> getVoxels() {
        return voxels;
    }
}
