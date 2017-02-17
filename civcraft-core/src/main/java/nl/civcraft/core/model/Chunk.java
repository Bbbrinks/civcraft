package nl.civcraft.core.model;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import nl.civcraft.core.utils.MathUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk implements Serializable {

    public static final int CHUNK_SIZE = 30;
    private final int chunkX;
    private final int chunkZ;
    private final GameObject[] voxels;
    private final int chunkY;
    private final String name;
    private final Transform transform;

    public Chunk(int chunkX, int chunkY, int chunkZ) {
        voxels = new GameObject[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkY = chunkY;
        int x = chunkX * CHUNK_SIZE;
        int y = chunkY * CHUNK_SIZE;
        int z = chunkZ * CHUNK_SIZE;
        this.name = chunkX + "x" + chunkY + "x" + chunkZ;
        this.transform = new Transform(new Vector3f(x, y, z));
    }

    public Transform getTransform() {
        return transform;
    }

    public void addVoxel(GameObject voxel) {
        voxels[getArrayIndex(voxel)] = voxel;
    }

    private int getArrayIndex(GameObject voxel) {
        Vector3f localTranslation = getLocalTranslation(voxel);
        return getArrayIndex(Math.round(localTranslation.getX()), Math.round(localTranslation.getY()),
                Math.round(localTranslation.getZ()));
    }

    private Vector3f getLocalTranslation(GameObject voxel) {
        return voxel.getTransform().getTranslation().subtract(transform.getTranslation());
    }

    private int getArrayIndex(int x, int y, int z) {
        int arrayIndex = (CHUNK_SIZE * CHUNK_SIZE * z) + (CHUNK_SIZE * y) + x;
        if (arrayIndex < 0 || arrayIndex >= voxels.length) {
            return -1;
        }
        return arrayIndex;
    }

    public Optional<GameObject> getVoxelAt(int x, int y, int z) {
        int arrayIndex = getArrayIndex(x - chunkX * CHUNK_SIZE, y - chunkY * CHUNK_SIZE, z - chunkZ * CHUNK_SIZE);
        if (arrayIndex == -1) {
            return Optional.empty();
        }
        return Optional.ofNullable(voxels[arrayIndex]);
    }

    public GameObject[] getVoxels() {
        return voxels;
    }


    public void removeVoxel(GameObject voxel) {
        int arrayIndex = getArrayIndex(voxel);
        voxels[arrayIndex] = null;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkY() {
        return chunkY;
    }

    public boolean containsCoors(int x, int y, int z) {
        int roundedX = Math.round(transform.getTranslation().getX());
        int roundedY = Math.round(transform.getTranslation().getY());
        int roundedZ = Math.round(transform.getTranslation().getZ());
        return MathUtil.between(roundedX, x, roundedX + CHUNK_SIZE) &&
                MathUtil.between(roundedY, y, roundedY + CHUNK_SIZE) &&
                MathUtil.between(roundedZ, z, roundedZ + CHUNK_SIZE);
    }

    public String getName() {
        return name;
    }
}
