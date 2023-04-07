package nl.civcraft.opengl.hud;

import org.joml.Intersectiond;
import org.joml.Rectanglef;
import org.joml.Vector2d;

import java.util.Objects;

/**
 * Created by Bob on 6-8-2018.
 * <p>
 * This is probably not worth documenting
 */
public abstract class HudElement {
    protected boolean hover;
    protected Rectanglef bounds;

    public boolean handleMouseMovement(Vector2d mousePosition) {
        hover = !Objects.isNull(bounds) &&
                Intersectiond.testPointAar(mousePosition.x, mousePosition.y, bounds.minX, bounds.minY, bounds.maxX, bounds.maxY);
        return hover;
    }

    public abstract boolean handleLeftClick();

    public abstract Rectanglef render(NanoVGContext nanoVGContext,
                                      int x,
                                      int y,
                                      int width,
                                      int height);

    public abstract void cleanup();

    public boolean isHover() {
        return hover;
    }

    public Rectanglef getBounds() {
        return bounds;
    }
}
