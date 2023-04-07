package nl.civcraft.opengl.hud.font;

import nl.civcraft.opengl.hud.NanoVGContext;
import nl.civcraft.opengl.hud.NavoVGUtil;
import org.joml.Vector4f;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Created by Bob on 10-3-2018.
 * <p>
 * This is probably not worth documenting
 */
public class Font {
    private final Vector4f fontColor;
    private final FontFamily fontFamily;
    private final float fontSize;
    private final NanoVGContext nanoVGContext;

    public Font(Vector4f fontColor,
                FontFamily fontFamily,
                float fontSize,
                NanoVGContext nanoVGContext) {
        this.fontColor = fontColor;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
        this.nanoVGContext = nanoVGContext;
    }

    public Vector4f getFontColor() {
        return fontColor;
    }


    public float getFontSize() {
        return fontSize;
    }

    public void selectFont() {
        nvgFontSize(nanoVGContext.getVg(), 25.0f);
        fontFamily.selectFontFamily();
        nvgTextAlign(nanoVGContext.getVg(), NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
        nvgFillColor(nanoVGContext.getVg(), NavoVGUtil.convertColor(fontColor));
    }
}
