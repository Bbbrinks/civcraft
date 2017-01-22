package nl.civcraft.jme3.gamecomponents;


import com.jme3.material.Material;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.model.RenderedVoxelFace;
import nl.civcraft.jme3.rendering.VoxelMaterialManager;
import nl.civcraft.jme3.utils.BlockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StaticVoxelRenderer extends VoxelRenderer implements nl.civcraft.core.rendering.VoxelRenderer {

    private final VoxelMaterialManager voxelMaterialManager;
    private Map<Face, RenderedVoxelFace> block;

    private StaticVoxelRenderer(VoxelMaterialManager voxelMaterialManager) {
        this.voxelMaterialManager = voxelMaterialManager;
    }


    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        Material topMaterial = voxelMaterialManager.topMaterial(voxel);
        Material sideMaterial = voxelMaterialManager.sideMaterial(voxel);
        Material bottomMaterial = voxelMaterialManager.bottomMaterial(voxel);

        block = BlockUtil.getQuadBlock(topMaterial, sideMaterial, bottomMaterial);
    }

    @Override
    public Map<Face, RenderedVoxelFace> getFaces() {
        return block;
    }

    @Component
    public static class StaticVoxelRenderFactoryImpl implements StaticVoxelRendererFactory<StaticVoxelRenderer> {
        private final VoxelMaterialManager voxelMaterialManager;

        @Autowired
        public StaticVoxelRenderFactoryImpl(VoxelMaterialManager voxelMaterialManager) {
            this.voxelMaterialManager = voxelMaterialManager;
        }

        @Override
        public StaticVoxelRenderer build() {
            return new StaticVoxelRenderer(voxelMaterialManager);
        }

        @Override
        public Class<StaticVoxelRenderer> getComponentType() {
            return StaticVoxelRenderer.class;
        }
    }

}
