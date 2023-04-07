package nl.civcraft.opengl.hud;

import org.joml.Vector4f;
import org.lwjgl.nanovg.NVGColor;

/**
 * Created by Bob on 6-8-2018.
 * <p>
 * This is probably not worth documenting
 */
public class NavoVGUtil {

    public static NVGColor convertColor(Vector4f colorVector) {
        NVGColor nvgColor = NVGColor.create();
        nvgColor.r(colorVector.x);
        nvgColor.g(colorVector.y);
        nvgColor.b(colorVector.z);
        nvgColor.a(colorVector.w);
        return nvgColor;
    }
}
