package nl.civcraft.jme3.rendering;

import com.jme3.asset.AssetManager;
import com.jme3.shadow.DirectionalLightShadowFilter;

/**
 * Created by Bob on 25-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class CCDirectionalLightShadowFilter extends DirectionalLightShadowFilter {
    /**
     * Creates a DirectionalLightShadowFilter Shadow Filter More info on the
     * technique at <a
     * href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a>
     *
     * @param assetManager  the application asset manager
     * @param shadowMapSize the size of the rendered shadowmaps (512,1024,2048,
     *                      etc...)
     * @param nbSplits      the number of shadow maps rendered (the more shadow maps
     */
    public CCDirectionalLightShadowFilter(AssetManager assetManager, int shadowMapSize, int nbSplits) {
        super(assetManager, shadowMapSize, nbSplits);
    }
}
