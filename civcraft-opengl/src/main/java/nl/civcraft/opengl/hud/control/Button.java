package nl.civcraft.opengl.hud.control;

import nl.civcraft.opengl.hud.HudElement;
import nl.civcraft.opengl.hud.NanoVGContext;
import nl.civcraft.opengl.hud.NavoVGUtil;
import nl.civcraft.opengl.hud.font.Font;
import org.joml.Rectanglef;
import org.joml.Vector4f;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Created by Bob on 10-3-2018.
 * <p>
 * This is probably not worth documenting
 */
public class Button extends HudElement {
    private final Runnable clickHandler;
    private String label;
    private Font font;
    private Vector4f backgroundColor;
    private Vector4f hoverBackgroundColor;


    public Button(String label,
                  Font font,
                  Vector4f backgroundColor,
                  Vector4f hoverBackgroundColor,
                  Runnable clickHandler) {
        this.label = label;
        this.font = font;
        this.backgroundColor = backgroundColor;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.clickHandler = clickHandler;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean handleLeftClick() {
        if(!hover) {
            return false;
        }
        clickHandler.run();
        return true;
    }

    @Override
    public Rectanglef render(NanoVGContext nanoVGContext,
                             int x,
                             int y,
                             int width,
                             int height) {
        this.bounds = new Rectanglef(x, y, x + width, y + height);
        // Block
        long vg = nanoVGContext.getVg();
        nvgBeginPath(vg);
        nvgRect(vg, x, y, width, height);
        if (!hover) {
            nvgFillColor(vg, NavoVGUtil.convertColor(backgroundColor));
        } else {
            nvgFillColor(vg, NavoVGUtil.convertColor(hoverBackgroundColor));
        }
        nvgFill(vg);

        // Text
        font.selectFont();
        nvgText(vg, x + (width / 2), y + (height / 2), label);
        return bounds;
    }

    @Override
    public void cleanup() {

    }
}
