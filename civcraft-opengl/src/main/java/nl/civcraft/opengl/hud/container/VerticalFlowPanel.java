package nl.civcraft.opengl.hud.container;

import nl.civcraft.opengl.hud.HudElement;
import nl.civcraft.opengl.hud.NanoVGContext;
import org.joml.Rectanglef;

/**
 * Created by Bob on 8-8-2018.
 * <p>
 * This is probably not worth documenting
 */
public class VerticalFlowPanel extends AbstractPanel {


    public VerticalFlowPanel() {
    }

    @Override
    public Rectanglef render(NanoVGContext nanoVGContext,
                             int x,
                             int y,
                             int width,
                             int height) {
        this.bounds = new Rectanglef(x, y, x + width, y + height);
        int currentChild = 0;
        int widthPerChild = width / children.size();
        for (HudElement child : children) {
            child.render(nanoVGContext, widthPerChild * currentChild, y, widthPerChild, height);
            currentChild++;
        }
        return bounds;
    }

    @Override
    public void cleanup() {

    }

}
