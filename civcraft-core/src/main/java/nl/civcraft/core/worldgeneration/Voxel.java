package nl.civcraft.core.worldgeneration;

import com.jme3.bounding.BoundingBox;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import nl.civcraft.core.model.Face;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends Node {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String type;

    public Voxel(int x, int y, int z, String type, Material material) {
        Box box1 = new Box(0.5f, 0.5f, 0.5f);
        Geometry blue = new Geometry("Box", box1);
        setLocalTranslation(new Vector3f(x, y, z));
        this.type = type;
        blue.setMaterial(material);
        this.attachChild(blue);
    }

    public String getType() {
        return type;
    }

    public boolean isCardinalNeighbour(Voxel voxel) {
        Vector3f subtract = voxel.getWorldTranslation().subtract(getWorldTranslation());
        Vector3f normalize = subtract.normalize();
        if (Math.abs(normalize.x) == 1.0f) {
            if (Math.abs(subtract.x) == (voxel.getMeshBound().getXExtent() + getMeshBound().getXExtent())) {
                return true;
            }
        } else if (Math.abs(normalize.y) == 1.0f) {
            if (Math.abs(subtract.y) == (voxel.getMeshBound().getYExtent() + getMeshBound().getYExtent())) {
                return true;
            }
        } else if (Math.abs(normalize.z) == 1.0f) {
            if (Math.abs(subtract.z) == (voxel.getMeshBound().getZExtent() + getMeshBound().getZExtent())) {
                return true;
            }
        }
        return false;
    }

    public void merge(Voxel voxel) {
        //Update mesh
        BoundingBox otherBound = voxel.getMeshBound();
        BoundingBox thisBound = getMeshBound();
        Vector3f normalize = voxel.getWorldTranslation().subtract(getWorldTranslation()).normalize();
        float xExtend = thisBound.getXExtent();
        float yExtend = thisBound.getYExtent();
        float zExtend = thisBound.getZExtent();
        if (Math.abs(normalize.x) == 1.0f) {
            xExtend += otherBound.getXExtent();
        } else if (Math.abs(normalize.y) == 1.0f) {
            yExtend += otherBound.getYExtent();
        } else if (Math.abs(normalize.z) == 1.0f) {
            zExtend += otherBound.getZExtent();
        }
        Box box = new Box(xExtend, yExtend, zExtend);
        Geometry box1 = (Geometry) this.getChild("Box");
        box1.setMesh(box);
        box1.getMaterial().setColor("Color", new ColorRGBA(xExtend, yExtend, zExtend, 1.0f));
        LOGGER.trace(String.format("Merge bounding box result: {%s, %s, %s}", xExtend, yExtend, zExtend));

        //Update location
        Vector3f localTranslation = getLocalTranslation();
        Vector3f otherTranslaction = voxel.getLocalTranslation();
        float newX = (localTranslation.x * thisBound.getXExtent() + otherTranslaction.x * otherBound.getXExtent()) / (thisBound.getXExtent() + otherBound.getXExtent());
        float newY = (localTranslation.y * thisBound.getYExtent() + otherTranslaction.y * otherBound.getYExtent()) / (thisBound.getYExtent() + otherBound.getYExtent());
        float newZ = (localTranslation.z * thisBound.getZExtent() + otherTranslaction.z * otherBound.getZExtent()) / (thisBound.getZExtent() + otherBound.getZExtent());
        Vector3f newTranslaction = new Vector3f(newX, newY, newZ);
        setLocalTranslation(newTranslaction);
        LOGGER.trace(String.format("Merge translation result: %s", newTranslaction));
    }

    private BoundingBox getMeshBound() {
        return (BoundingBox) descendantMatches(Geometry.class).get(0).getMesh().getBound();
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

    private boolean isFaceSameSize(Voxel voxel, Face face) {
        if (face.equals(Face.BACK) || face.equals(Face.FRONT)) {
            return getMeshBound().getYExtent() == voxel.getMeshBound().getYExtent();
        } else if (face.equals(Face.UP) || face.equals(Face.DOWN)) {
            return getMeshBound().getZExtent() == voxel.getMeshBound().getZExtent();
        } else if (face.equals(Face.LEFT) || face.equals(Face.RIGHT)) {
            return getMeshBound().getXExtent() == voxel.getMeshBound().getXExtent();
        }
        return false;
    }

    /**
     * Returns the the side on which the provided voxel is.
     * If the centers of these voxels are no alligned Face.NONE is returned
     *
     * @param voxel
     * @return
     */
    private Face getFacingSide(Voxel voxel) {
        Vector3f subtract = voxel.getWorldTranslation().subtract(getWorldTranslation()).normalize();
        if (subtract.x == 1.0f) {
            return Face.FRONT;
        } else if (subtract.x == -1.0f) {
            return Face.BACK;
        } else if (subtract.y == 1.0f) {
            return Face.UP;
        } else if (subtract.y == -1.0f) {
            return Face.DOWN;
        } else if (subtract.z == 1.0f) {
            return Face.LEFT;
        } else if (subtract.z == -1.0f) {
            return Face.RIGHT;
        }
        return Face.NONE;
    }

    @Override
    public String toString() {
        return String.format("{type: %s, translation: %s, bound: %s}", getType(), getWorldTranslation(), getMeshBound());
    }

    public int getX() {
        return (int) getWorldTranslation().getX();
    }

    public int getY() {
        return (int) getWorldTranslation().getY();
    }

    public int getZ() {
        return (int) getWorldTranslation().getZ();
    }
}
