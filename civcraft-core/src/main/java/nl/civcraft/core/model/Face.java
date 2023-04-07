package nl.civcraft.core.model;


import org.joml.Vector3f;

/**
 * Created by Bob on 26-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public enum Face {
    TOP(new Vector3f(0, 1, 0)), BOTTOM(new Vector3f(0, -1, 0)), LEFT(new Vector3f(-1, 0, 0)), RIGHT(new Vector3f(1, 0, 0)), FRONT(new Vector3f(0, 0, -1)), BACK(new Vector3f(0, 0, 1));

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
