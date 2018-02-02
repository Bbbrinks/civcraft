package nl.civcraft.boundingbox;

import org.joml.Vector3f;

/**
 * Created by Bob on 2-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class Ray {
    private final Vector3f origin;
    private final Vector3f direction;       // ray origin and direction
    private final Vector3f invertedDirection;
    private final int[] sign;

    public Ray(Vector3f origin,
               Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
        sign = new int[3];
        invertedDirection = direction.mul(-1, new Vector3f());
        sign[0] = (invertedDirection.x < 0) ? 0 : 1;
        sign[1] = (invertedDirection.y < 0) ? 0 : 1;
        sign[2] = (invertedDirection.z < 0) ? 0 : 1;
    }

    public Vector3f getOrigin() {
        return origin;
    }

    public Vector3f getDirection() {
        return direction;
    }

    public Vector3f getInvertedDirection() {
        return invertedDirection;
    }

    public int[] getSign() {
        return sign;
    }
}
