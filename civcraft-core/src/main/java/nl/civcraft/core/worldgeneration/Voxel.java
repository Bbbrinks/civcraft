package nl.civcraft.core.worldgeneration;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
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
    private Material material;

    public Voxel(int x, int y, int z, String type, Material material) {
        Box box1 = new Box(0.5f, 0.5f, 0.5f);
        Geometry blue = new Geometry("Box", box1);
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        blue.setMaterial(material);
        this.material = material;
        this.geometry = blue;
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

    public Geometry getGeometry() {
        return geometry;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
