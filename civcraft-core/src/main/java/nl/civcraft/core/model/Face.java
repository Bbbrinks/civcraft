package nl.civcraft.core.model;

import com.jme3.math.Vector3f;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public enum Face {
    TOP(Vector3f.UNIT_Y), BOTTOM(Vector3f.UNIT_Y.mult(-1)), LEFT(Vector3f.UNIT_X.mult(-1)), RIGHT(Vector3f.UNIT_X), FRONT(Vector3f.UNIT_Z.mult(-1)), BACK(Vector3f.UNIT_Z);

    static {
        TOP.setOpposite(BOTTOM);
        BOTTOM.setOpposite(TOP);
        LEFT.setOpposite(RIGHT);
        RIGHT.setOpposite(LEFT);
        FRONT.setOpposite(BACK);
        BACK.setOpposite(FRONT);
    }

    private final Vector3f translation;
    private Face opposite;

    Face(Vector3f translation) {
        this.translation = translation;
    }

    public Vector3f getTranslation() {
        return translation;
    }

    public Face getOpposite() {
        return opposite;
    }

    private void setOpposite(Face opposite) {
        this.opposite = opposite;
    }
}
