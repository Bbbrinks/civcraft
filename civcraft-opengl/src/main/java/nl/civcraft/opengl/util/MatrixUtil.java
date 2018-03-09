package nl.civcraft.opengl.util;

import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.rendering.Camera;
import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by Bob on 3-2-2018.
 * <p>
 * This is probably not worth documenting
 */
public class MatrixUtil {

    public static final double FOV = 60.0;

    private MatrixUtil() {
        
    }
    
    public static Matrix4f getViewMatrix(Camera camera){
        Vector3f cameraPos = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        // First do the rotation so camera rotates over its position
        Matrix4f viewMatrix = new Matrix4f().rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
        // Then do the translation
        viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        return viewMatrix;
    }

    public static Matrix4f getProjectionMatrix(Window window){
        return new Matrix4f().perspective((float)Math.toRadians(FOV),
                window.getWidth() / (float)window.getHeight(),
                0.1f,
                100.0f
                );
    }

    public static AABBf moveAndScaleBoundingBox(AABBf boundingBox, Matrix4f transfrom) {
        Vector3f min = new Vector3f(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        Vector3f max = new Vector3f(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        min.mul(transfrom.getScale(new Vector3f())).add(transfrom.getTranslation(new Vector3f()));
        max.mul(transfrom.getScale(new Vector3f())).add(transfrom.getTranslation(new Vector3f()));
        return new AABBf(min,max);
    }

    public static Matrix4f getLightViewMatrix(Vector3f position,
                                              Vector3f rotation) {
        return new Matrix4f().rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }

    public static Matrix4f getOrthoProjectionMatrix(float left,
                                                    float right,
                                                    float bottom,
                                                    float top,
                                                    float zNear,
                                                    float zFar) {
        return new Matrix4f().setOrtho(left, right, bottom, top, zNear, zFar);
    }
}
