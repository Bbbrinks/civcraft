package nl.civcraft.test.util;


import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Bob on 19-2-2017.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelUtil {
    public static void createVoxels(boolean[][][] positions, VoxelManager voxelManager) {
        int x = 0;
        int y = 0;
        int z = 0;
        for (boolean[][] yPositions : positions) {
            for (boolean[] xPosition : yPositions) {
                for (boolean zPotions : xPosition) {
                    if (zPotions) {
                        createVoxel(new Vector3f(x, y, z), voxelManager);
                    }
                    x++;
                }
                x = 0;
                z++;
            }
            z = 0;
            y++;
        }
    }

    public static GameObject createVoxel(Vector3f translation, VoxelManager voxelManager) {
        GameObject gameObject = new GameObject(new Matrix4f().translate(translation));
        gameObject.addComponent(new Voxel("test", voxelManager));
        return gameObject;
    }
}
