package nl.civcraft.opengl.rendering;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Box extends Mesh {
    private static final float[] positions = new float[]{
            // V0
            -0.25f, 0.25f, 0.25f,
            // V1
            -0.25f, -0.25f, 0.25f,
            // V2
            0.25f, -0.25f, 0.25f,
            // V3
            0.25f, 0.25f, 0.25f,
            // V4
            -0.25f, 0.25f, -0.25f,
            // V5
            0.25f, 0.25f, -0.25f,
            // V6
            -0.25f, -0.25f, -0.25f,
            // V7
            0.25f, -0.25f, -0.25f,

            // For text coords in top face
            // V8: V4 repeated
            -0.25f, 0.25f, -0.25f,
            // V9: V5 repeated
            0.25f, 0.25f, -0.25f,
            // V10: V0 repeated
            -0.25f, 0.25f, 0.25f,
            // V11: V3 repeated
            0.25f, 0.25f, 0.25f,

            // For text coords in right face
            // V12: V3 repeated
            0.25f, 0.25f, 0.25f,
            // V13: V2 repeated
            0.25f, -0.25f, 0.25f,

            // For text coords in left face
            // V14: V0 repeated
            -0.25f, 0.25f, 0.25f,
            // V15: V1 repeated
            -0.25f, -0.25f, 0.25f,

            // For text coords in bottom face
            // V16: V6 repeated
            -0.25f, -0.25f, -0.25f,
            // V17: V7 repeated
            0.25f, -0.25f, -0.25f,
            // V18: V1 repeated
            -0.25f, -0.25f, 0.25f,
            // V19: V2 repeated
            0.25f, -0.25f, 0.25f,
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
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,};

    private static Box INSTANCE;

    private Box() {
        super(positions, textCoords, indices);
    }

    public static Box instance() {
        if(Box.INSTANCE == null){
            Box.INSTANCE = new Box();
        }
        return Box.INSTANCE;
    }
}