package nl.civcraft.core.model;

import com.jme3.scene.Geometry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String type;
    private final int x;
    private final int y;
    private final int z;
    private final Geometry geometry;

    public Voxel(int x, int y, int z, String type, Geometry geometry) {

        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;

        this.geometry = geometry;
    }

    public String getType() {
        return type;
    }


    public boolean canMerge(Voxel voxel) {
        if (voxel == null) {
            return false;
        }
        if (equals(voxel)) {
            return false;
        } else if (!getType().equals(voxel.getType())) {
            return false;
        } else {
            return true;
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Geometry cloneGeometry() {
        return geometry.clone();
    }

}
