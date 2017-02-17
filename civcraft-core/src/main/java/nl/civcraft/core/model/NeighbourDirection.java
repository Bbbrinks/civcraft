package nl.civcraft.core.model;

import com.jme3.math.Vector3f;

import java.util.Optional;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public enum NeighbourDirection {
    TOP(Vector3f.UNIT_Y),
    BOTTOM(Vector3f.UNIT_Y.mult(-1)),
    LEFT(Vector3f.UNIT_X.mult(-1)),
    RIGHT(Vector3f.UNIT_X),
    FRONT(Vector3f.UNIT_Z.mult(-1)),
    BACK(Vector3f.UNIT_Z),
    LEFT_TOP(new Vector3f(-1, 1, 0)),
    RIGHT_TOP(new Vector3f(1, 1, 0)),
    LEFT_BOTTOM(new Vector3f(-1, -1, 0)),
    RIGHT_BOTTOM(new Vector3f(1, -1, 0)),
    FRONT_TOP(new Vector3f(0, 1, -1)),
    BACK_TOP(new Vector3f(0, 1, 1)),
    FRONT_BOTTOM(new Vector3f(0, -1, -1)),
    BACK_BOTTOM(new Vector3f(0, -1, 1));

    static {
        TOP.setOpposite(BOTTOM);
        BOTTOM.setOpposite(TOP);
        LEFT.setOpposite(RIGHT);
        RIGHT.setOpposite(LEFT);
        FRONT.setOpposite(BACK);
        BACK.setOpposite(FRONT);
        LEFT_TOP.setOpposite(RIGHT_BOTTOM);
        RIGHT_TOP.setOpposite(LEFT_BOTTOM);
        LEFT_BOTTOM.setOpposite(RIGHT_TOP);
        RIGHT_BOTTOM.setOpposite(LEFT_TOP);
        FRONT_TOP.setOpposite(BACK_BOTTOM);
        BACK_TOP.setOpposite(FRONT_BOTTOM);
        FRONT_BOTTOM.setOpposite(BACK_TOP);
        BACK_BOTTOM.setOpposite(FRONT_TOP);
    }


    private final Vector3f translation;
    private NeighbourDirection opposite;

    NeighbourDirection(Vector3f translation) {
        this.translation = translation;
    }

    public static Optional<NeighbourDirection> fromFace(Face face) {
        for (NeighbourDirection neighbourDirection : NeighbourDirection.values()) {
            if (neighbourDirection.getTranslation().equals(face.getTranslation())) {
                return Optional.of(neighbourDirection);
            }
        }
        return Optional.empty();
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public NeighbourDirection getOpposite() {
        return opposite;
    }

    public void setOpposite(NeighbourDirection opposite) {
        this.opposite = opposite;
    }
}