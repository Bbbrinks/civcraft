package nl.civcraft.opengl.hud.font;

import nl.civcraft.opengl.hud.NanoVGContext;
import nl.civcraft.opengl.util.ResourceUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;
import static org.lwjgl.nanovg.NanoVG.nvgFontFace;

/**
 * Created by Bob on 10-3-2018.
 * <p>
 * This is probably not worth documenting
 */
public class FontFamily {
    private final int fontId;
    private final ByteBuffer fontBuffer;
    private final String fontName;
    private final NanoVGContext nanoVGContext;

    public FontFamily(String fontUrl,
                      String fontName,
                      NanoVGContext nanoVGContext) throws IOException {
        fontBuffer = ResourceUtil.ioResourceToByteBuffer(fontUrl, 150 * 1024);
        fontId = nvgCreateFontMem(nanoVGContext.getVg(), fontName, fontBuffer, 0);
        if (fontId == -1) {
            throw new IllegalStateException("Could not add font: " + fontUrl);
        }
        this.fontName = fontName;
        this.nanoVGContext = nanoVGContext;
    }


    public int getFontId() {
        return fontId;
    }

    public void selectFontFamily() {
        nvgFontFace(nanoVGContext.getVg(), fontName);
    }
}
