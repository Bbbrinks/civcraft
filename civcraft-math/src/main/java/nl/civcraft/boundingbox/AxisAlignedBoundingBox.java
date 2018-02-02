package nl.civcraft.boundingbox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Bob on 2-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class AxisAlignedBoundingBox {
    private final Vector3f[] bounds;
    private final float distance;

    public AxisAlignedBoundingBox(Vector3f boundMin,
                                  Vector3f boundMax) {
        bounds = new Vector3f[2];
        bounds[0] = boundMin;
        bounds[1] = boundMax;
        distance = boundMin.distance(boundMax);
    }

    public AxisAlignedBoundingBox() {
        this(new Vector3f(), new Vector3f());
    }


    public boolean intersect(Ray ray) {
        float intersectionMinX = (bounds[ray.getSign()[0]].x - ray.getOrigin().x) * ray.getInvertedDirection().x;
        float intersectionMaxX = (bounds[1 - ray.getSign()[0]].x - ray.getOrigin().x) * ray.getInvertedDirection().x;
        float intersectionMinY = (bounds[ray.getSign()[1]].y - ray.getOrigin().y) * ray.getInvertedDirection().y;
        float intersectionMaxY = (bounds[1 - ray.getSign()[1]].y - ray.getOrigin().y) * ray.getInvertedDirection().y;

        if ((intersectionMinX > intersectionMaxY) || (intersectionMinY > intersectionMaxX)) {
            return false;
        }
        if (intersectionMinY > intersectionMinX) {
            intersectionMinX = intersectionMinY;
        }
        if (intersectionMaxY < intersectionMaxX) {
            intersectionMaxX = intersectionMaxY;
        }

        float intersectionMinZ = (bounds[ray.getSign()[2]].z - ray.getOrigin().z) * ray.getInvertedDirection().z;
        float intersectionMaxZ = (bounds[1 - ray.getSign()[2]].z - ray.getOrigin().z) * ray.getInvertedDirection().z;

        return (intersectionMinX <= intersectionMaxZ) && (intersectionMinZ <= intersectionMaxX);
    }

    public AxisAlignedBoundingBox add(AxisAlignedBoundingBox boundingBox) {
        Vector3f boundMin = new Vector3f(bounds[0]).min(boundingBox.bounds[0]);
        Vector3f boundMax = new Vector3f(bounds[1]).max(boundingBox.bounds[1]);
        return new AxisAlignedBoundingBox(boundMin, boundMax);
    }


    public AxisAlignedBoundingBox transform(Matrix4f transform) {
        Vector3f scale = transform.getScale(new Vector3f());
        Vector3f boundMin = new Vector3f(bounds[0]).mul(scale, new Vector3f());
        Vector3f boundMax = new Vector3f(bounds[1]).mul(scale, new Vector3f());

        Vector3f translation = transform.getTranslation(new Vector3f());
        boundMin = boundMin.add(translation);
        boundMax = boundMax.add(translation);
        return new AxisAlignedBoundingBox(boundMin, boundMax);
    }

    public boolean includes(Vector3f includingCoordinates) {
        if(bounds[0].distance(includingCoordinates) > distance) {
            return false;
        }
        return bounds[0].x < includingCoordinates.x && bounds[1].x > includingCoordinates.x &&
                bounds[0].y < includingCoordinates.y && bounds[1].y > includingCoordinates.y &&
                bounds[0].z < includingCoordinates.z && bounds[1].z > includingCoordinates.z;
    }


}
