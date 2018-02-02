package nl.civcraft.boundingbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Bob on 2-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class BoundingBoxUtil {
    private BoundingBoxUtil(){

    }

    public static AxisAlignedBoundingBox buildBoundingBox(Matrix4f transform, float size) {
        float halfSize = size/2;
        Vector3f translation = transform.getTranslation(new Vector3f());

        return new AxisAlignedBoundingBox(translation.sub(halfSize, halfSize, halfSize), translation.add(halfSize, halfSize,halfSize));
    }
}
