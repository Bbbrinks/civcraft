package nl.civcraft.jme3.nl.civcraft.jme3;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.jme3.nl.civcraft.jme3.utils.MaterialUtil;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 20-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelMaterialManager {
    private static final String TEXTURES = "textures/";
    private final AssetManager assetManager;
    private final Map<String, Material> loadedMaterials = new HashMap<>();

    @Inject
    public VoxelMaterialManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Material topMaterial(Voxel voxel) {
        return loadMaterial(voxel.getType(), "top");
    }

    public Material loadMaterial(String... materialNames) {
        if (loadedMaterials.containsKey(String.join("-", materialNames))) {
            return loadedMaterials.get(String.join("-", materialNames));
        }
        String[] possibleNames = materialNames;
        while (possibleNames.length > 0) {
            String join = String.join("-", possibleNames);
            AssetInfo assetInfo = assetManager.locateAsset(new TextureKey(TEXTURES + join + ".png"));
            if (assetInfo != null) {
                Material material = MaterialUtil.getPhongIllumnitedMaterial(assetManager, TEXTURES + join + ".png");
                loadedMaterials.put(String.join("-", materialNames), material);
                return material;
            }
            possibleNames = Arrays.copyOf(possibleNames, possibleNames.length - 1);
        }
        throw new IllegalStateException("Could not find texture " + String.join("-", materialNames));
    }

    public Material sideMaterial(Voxel voxel) {
        return loadMaterial(voxel.getType(), "side");
    }

    public Material bottomMaterial(Voxel voxel) {
        return loadMaterial(voxel.getType(), "bottom");
    }
}
