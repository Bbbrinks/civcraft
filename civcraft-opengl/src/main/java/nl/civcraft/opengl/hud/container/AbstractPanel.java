package nl.civcraft.opengl.hud.container;

import nl.civcraft.opengl.hud.HudElement;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 8-8-2018.
 * <p>
 * This is probably not worth documenting
 */
public abstract class AbstractPanel extends HudElement {

    protected final List<HudElement> children;

    protected AbstractPanel() {
        children = new ArrayList<>();
    }

    @Override
    public boolean handleLeftClick() {
        if(!hover) {
            return false;
        }
        for (HudElement child : children) {
            if (child.handleLeftClick()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleMouseMovement(Vector2d mousePosition) {
        boolean hover = super.handleMouseMovement(mousePosition);
        for (HudElement child : children) {
            child.handleMouseMovement(mousePosition);
        }
        return hover;
    }


    public void addChild(HudElement child) {
        children.add(child);
    }

}
