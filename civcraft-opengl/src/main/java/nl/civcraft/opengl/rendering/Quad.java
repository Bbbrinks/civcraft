package nl.civcraft.opengl.rendering;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Quad extends Mesh {

    private static Quad RIGHT;

    private static int[] rightIndices = new int[]{
            0, 1, 2, 3, 0, 2
    };

    private static float[] rightPositions = new float[] {
            // V12
            0.25f, 0.25f, 0.25f,
            // V13
            0.25f, -0.25f, 0.25f,
            // V7
            0.25f, -0.25f, -0.25f,
            // V5
            0.25f, 0.25f, -0.25f,
    };

    private static float[] rightTextCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

    };


    private static Quad LEFT;

    private static int[] leftIndices = new int[]{
            0, 1, 2, 3, 0, 2
    };

    private static float[] leftPositions = new float[] {
            // V14
            -0.25f, 0.25f, 0.25f,
            // V15
            -0.25f, -0.25f, 0.25f,
            // V6
            -0.25f, -0.25f, -0.25f,
            // V4
            -0.25f, 0.25f, -0.25f,
    };

    private static float[] leftTextCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,

    };

    private static int[] backIndices = new int[]{
            0, 1, 3, 3, 1, 2
    };

    private static float[] backPostions = new float[] {
            // V0
            -0.25f, 0.25f, 0.25f,
            // V1
            -0.25f, -0.25f, 0.25f,
            // V2
            0.25f, -0.25f, 0.25f,
            // V3
            0.25f, 0.25f, 0.25f,
    };

    private static float[] backTextCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f
    };

    private static Quad BACK;

    private static int[] frontIndices = new int[]{
            0, 1, 2, 3, 0, 2
    };

    private static float[] frontPostions = new float[] {
            // V4
            -0.25f, 0.25f, -0.25f,
            // V6
            -0.25f, -0.25f, -0.25f,
            // V7
            0.25f, -0.25f, -0.25f,
            // V5
            0.25f, 0.25f, -0.25f,
    };

    private static float[] frontTextCoords = new float[] {
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f
    };


    private static Quad TOP;

    private static int[] topIndices = new int[]{
            0, 1, 2, 3, 0, 2
    };

    private static float[] topPositions = new float[] {
            // V8
            -0.25f, 0.25f, -0.25f,
            // V10
            -0.25f, 0.25f, 0.25f,
            // V11
            0.25f, 0.25f, 0.25f,
            // V9
            0.25f, 0.25f, -0.25f,
    };

    private static float[] topTextCoords = new float[] {
            0.0f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            0.5f, 0.5f

    };

    private static Quad BOTTOM;

    private static int[] bottomIndices = new int[]{
            0, 1, 2, 3, 0, 2
    };

    private static float[] bottomPositions = new float[] {
            // V16
            -0.25f, -0.25f, -0.25f,
            // V18
            -0.25f, -0.25f, 0.25f,
            // V19
            0.25f, -0.25f, 0.25f,
            // V17
            0.25f, -0.25f, -0.25f,
    };

    private static float[] bottomTextCoords = new float[] {
            0.5f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f,
            1.0f, 0.0f
    };
    private static Quad FRONT;


    private Quad(float[] positions,
                float[] textCoords,
                int[] indices) {
        super(positions, textCoords, indices);
    }

    public static Quad front() {
        if(Quad.FRONT == null){
            Quad.FRONT = new Quad(frontPostions, frontTextCoords, frontIndices);
        }
        return Quad.FRONT;
    }

    public static Quad back() {
        if(Quad.BACK == null){
            Quad.BACK = new Quad(backPostions, backTextCoords, backIndices);
        }
        return Quad.BACK;
    }

    public static Quad top() {
        if(Quad.TOP == null){
            Quad.TOP = new Quad(topPositions, topTextCoords, topIndices);
        }
        return Quad.TOP;
    }

    public static Quad bottom() {
        if(Quad.BOTTOM == null){
            Quad.BOTTOM = new Quad(bottomPositions, bottomTextCoords, bottomIndices);
        }
        return Quad.BOTTOM;
    }

    public static Quad left() {
        if(Quad.LEFT == null){
            Quad.LEFT = new Quad(leftPositions, leftTextCoords, leftIndices);
        }
        return Quad.LEFT;
    }

    public static Quad right() {
        if(Quad.RIGHT == null){
            Quad.RIGHT = new Quad(rightPositions, rightTextCoords, rightIndices);
        }
        return Quad.RIGHT;
    }
}
