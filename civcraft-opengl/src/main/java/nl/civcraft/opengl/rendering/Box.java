package nl.civcraft.opengl.rendering;

import org.joml.AABBf;
import org.joml.Vector3f;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Box extends Mesh {
    private static final float[] positions = new float[]{
            // V0
            -0.5f, 0.5f, 0.5f,
            // V1
            -0.5f, -0.5f, 0.5f,
            // V2
            0.5f, -0.5f, 0.5f,
            // V3
            0.5f, 0.5f, 0.5f,
            // V4
            -0.5f, 0.5f, -0.5f,
            // V5
            0.5f, 0.5f, -0.5f,
            // V6
            -0.5f, -0.5f, -0.5f,
            // V7
            0.5f, -0.5f, -0.5f,

            // For text coords in top face
            // V8: V4 repeated
            -0.5f, 0.5f, -0.5f,
            // V9: V5 repeated
            0.5f, 0.5f, -0.5f,
            // V10: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V11: V3 repeated
            0.5f, 0.5f, 0.5f,

            // For text coords in right face
            // V12: V3 repeated
            0.5f, 0.5f, 0.5f,
            // V13: V2 repeated
            0.5f, -0.5f, 0.5f,

            // For text coords in left face
            // V14: V0 repeated
            -0.5f, 0.5f, 0.5f,
            // V15: V1 repeated
            -0.5f, -0.5f, 0.5f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.5f, -0.5f, -0.5f,
            // V17: V7 repeated
            0.5f, -0.5f, -0.5f,
            // V18: V1 repeated
            -0.5f, -0.5f, 0.5f,
            // V19: V2 repeated
            0.5f, -0.5f, 0.5f,
    };
    private static float[] textCoords = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,

            // For text coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,

            // For text coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,

            // For text coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,

            // For text coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
    };
    private static int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            6, 14, 4, 6, 15, 14,
            // Bottom face
            19, 16, 17, 19, 18, 16,
            // Back face
            7, 4, 5, 7, 6, 4,};

    private static float[] normals = new float[]{
            // Front face
            1, 1, 1, 1, 1, 1,
            // Top Face
            1, 1, 1, 1, 1, 1,
            // Right face
            1, 1, 1, 1, 1, 1,
            // Left face
            1, 1, 1, 1, 1, 1,
            // Bottom face
            1, 1, 1, 1, 1, 1,
            // Back face
            1, 1, 1, 1, 1, 1,};

    private static Box INSTANCE;

    private Box() {
        super(positions, textCoords, indices, normals, new AABBf(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.5f, 0.5f, 0.5f)));
    }

    public static Box instance() {
        if (Box.INSTANCE == null) {
            Box.INSTANCE = new Box();
        }
        return Box.INSTANCE;
    }
}
