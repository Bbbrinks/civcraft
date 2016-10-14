package nl.civcraft.core.utils;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

/**
 * Created by Bob on 14-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public class MaterialUtil {

    public static final String COLOR = "Color";
    public static final String TEXTURE_MAP = "TextureMap";
    public static final String GRAY_SCALE_MAT_DEF = "matdefs/GrayScaleColorMap.j3md";
    private static final String UNSHADED_MAT_DEF = "Common/MatDefs/Misc/Unshaded.j3md";
    private static final String COLOR_MAP = "ColorMap";

    private MaterialUtil() {
        //hide constructor of util
    }

    public static Material getUnshadedMaterial(AssetManager assetManager, String name) {
        Texture texture = assetManager.loadTexture(
                name);
        Material material = new Material(assetManager,
                UNSHADED_MAT_DEF);
        material.setTexture(COLOR_MAP, texture);
        return material;
    }

    public static Material getGrayScaleMaterial(AssetManager assetManager, ColorRGBA color, String texture) {
        Texture grassTopTex = assetManager.loadTexture(
                texture);
        Material grassTopMaterial = new Material(assetManager,
                GRAY_SCALE_MAT_DEF);
        grassTopMaterial.setTexture(TEXTURE_MAP, grassTopTex);
        grassTopMaterial.setColor(COLOR, color);
        return grassTopMaterial;
    }

    public static Material getColoredMaterial(AssetManager assetManager, ColorRGBA color) {
        Material mat = new Material(assetManager,
                UNSHADED_MAT_DEF);
        mat.setColor("Color", color);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        return mat;
    }
}
