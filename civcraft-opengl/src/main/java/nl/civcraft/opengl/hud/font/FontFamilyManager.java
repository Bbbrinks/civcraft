package nl.civcraft.opengl.hud.font;

import nl.civcraft.opengl.hud.NanoVGContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 10-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class FontFamilyManager {
    private final NanoVGContext nanoVGContext;
    private final Map<String, FontFamily> fontFamilyMap;

    @Inject
    public FontFamilyManager(NanoVGContext nanoVGContext) {
        this.nanoVGContext = nanoVGContext;
        fontFamilyMap = new HashMap<>();
    }


    public FontFamily loadFont(String fontName, String fileName) throws IOException {
        if(fontFamilyMap.containsKey(fontName)){
            return fontFamilyMap.get(fontName);
        }
        FontFamily fontFamily = new FontFamily(fileName, fontName, nanoVGContext);
        fontFamilyMap.put(fontName, fontFamily);
        return fontFamily;
    }
}
