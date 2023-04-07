package nl.civcraft.opengl.hud.container;

import nl.civcraft.opengl.hud.HudElement;
import nl.civcraft.opengl.hud.NanoVGContext;
import org.joml.Rectanglef;

/**
 * Created by Bob on 8-8-2018.
 * <p>
 * This is probably not worth documenting
 */
public class PercentagePanel extends AbstractPanel {

    private float verticalPercentage;
    private float horizontalPercentage;

    public PercentagePanel(float verticalPercentage,
                           float horizontalPercentage) {
        this.verticalPercentage = verticalPercentage;
        this.horizontalPercentage = horizontalPercentage;
    }

    @Override
    public Rectanglef render(NanoVGContext nanoVGContext,
                             int x,
                             int y,
                             int width,
                             int height) {
        int actualWidth = Math.round(width * horizontalPercentage);
        int actualHeight = Math.round(height * verticalPercentage);
        for (HudElement child : children) {
            child.render(nanoVGContext, x, y, actualWidth, actualHeight);
        }
        this.bounds = new Rectanglef(x, y, x + actualWidth, y + actualHeight);
        return bounds;
    }

    @Override
    public void cleanup() {

    }
}
