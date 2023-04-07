package nl.civcraft.core.model;


import nl.civcraft.core.utils.MathUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Optional;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk {

    public static final int CHUNK_SIZE = 20;
    private final int chunkX;
    private final int chunkZ;
    private final GameObject[] voxels;
    private final int chunkY;
    private final String name;
    private final Matrix4f transform;

    public Chunk(int chunkX,
                 int chunkY,
                 int chunkZ) {
        voxels = new GameObject[CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.chunkY = chunkY;
        int x = chunkX * CHUNK_SIZE;
        int y = chunkY * CHUNK_SIZE;
        int z = chunkZ * CHUNK_SIZE;
        this.name = chunkX + "x" + chunkY + "x" + chunkZ;
        this.transform = new Matrix4f();
        this.transform.setTranslation(new Vector3f(x, y, z));
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public void addVoxel(GameObject voxel) {
        voxels[getArrayIndex(voxel)] = voxel;
    }

    private int getArrayIndex(GameObject voxel) {
        Vector3f localTranslation = getLocalTranslation(voxel);
        return getArrayIndex(Math.round(localTranslation.x()), Math.round(localTranslation.y()),
                Math.round(localTranslation.z()));
    }

    private Vector3f getLocalTranslation(GameObject voxel) {
        return voxel.getTransform().getTranslation(new Vector3f()).sub(transform.getTranslation(new Vector3f()));
    }

    private int getArrayIndex(int x,
                              int y,
                              int z) {
        int arrayIndex = (CHUNK_SIZE * CHUNK_SIZE * z) + (CHUNK_SIZE * y) + x;
        if (arrayIndex < 0 || arrayIndex >= voxels.length) {
            return -1;
        }
        return arrayIndex;
    }

    public Optional<GameObject> getVoxelAt(int x,
                                           int y,
                                           int z) {
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

    public boolean containsCoors(int x,
                                 int y,
                                 int z) {
        Vector3f chunkTransform = transform.getTranslation(new Vector3f());
        int roundedX = Math.round(chunkTransform.x());
        int roundedY = Math.round(chunkTransform.y());
        int roundedZ = Math.round(chunkTransform.z());
        return MathUtil.between(roundedX, x, roundedX + CHUNK_SIZE) &&
                MathUtil.between(roundedY, y, roundedY + CHUNK_SIZE) &&
                MathUtil.between(roundedZ, z, roundedZ + CHUNK_SIZE);
    }

    public String getName() {
        return name;
    }
}
