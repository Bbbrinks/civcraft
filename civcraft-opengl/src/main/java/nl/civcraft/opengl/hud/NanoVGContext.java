package nl.civcraft.opengl.hud;

import javax.inject.Singleton;

import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Bob on 10-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class NanoVGContext {

    private long vg;

    public void init(){
        this.vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES );
        if (this.vg == NULL) {
            throw new IllegalStateException("Could not init nanovg");
        }
    }

    public long getVg() {
        return vg;
    }
}
