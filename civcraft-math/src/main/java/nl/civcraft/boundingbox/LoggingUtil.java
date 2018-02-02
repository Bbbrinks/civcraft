package nl.civcraft.boundingbox;

import org.joml.Vector3f;

/**
 * Created by Bob on 2-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class LoggingUtil {

    public static String logableVector(Vector3f vector3f) {
        return String.format("%.4f, %.4f, %.4f", vector3f.x, vector3f.y, vector3f.z);
    }
}
