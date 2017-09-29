package nl.civcraft.jme3.nl.civcraft.jme3.gamecomponents;


import com.jme3.material.Material;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.nl.civcraft.jme3.model.RenderedVoxelFace;
import nl.civcraft.jme3.nl.civcraft.jme3.VoxelMaterialManager;
import nl.civcraft.jme3.nl.civcraft.jme3.VoxelRendererControl;
import nl.civcraft.jme3.nl.civcraft.jme3.utils.BlockUtil;

import javax.inject.Inject;
import java.util.Map;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StaticVoxelRenderer extends VoxelRenderer implements nl.civcraft.core.rendering.VoxelRenderer {

    private final VoxelMaterialManager voxelMaterialManager;
    private Map<Face, RenderedVoxelFace> block;

    private StaticVoxelRenderer(VoxelMaterialManager voxelMaterialManager, VoxelRendererControl voxelRendererControl) {
        super(voxelRendererControl);
        this.voxelMaterialManager = voxelMaterialManager;
    }


    @Override
    public void addTo(GameObject gameObject) {
        this.gameObject = gameObject;
        Material topMaterial = voxelMaterialManager.topMaterial(getVoxel());
        Material sideMaterial = voxelMaterialManager.sideMaterial(getVoxel());
        Material bottomMaterial = voxelMaterialManager.bottomMaterial(getVoxel());

        block = BlockUtil.getQuadBlock(topMaterial, sideMaterial, bottomMaterial);
        super.addTo(gameObject);
    }

    @Override
    public Map<Face, RenderedVoxelFace> getFaces() {
        return block;
    }

    public static class StaticVoxelRenderFactoryImpl implements StaticVoxelRendererFactory<StaticVoxelRenderer> {
        private final VoxelMaterialManager voxelMaterialManager;
        private final VoxelRendererControl voxelRenderControl;

        @Inject
        public StaticVoxelRenderFactoryImpl(VoxelMaterialManager voxelMaterialManager, VoxelRendererControl voxelRenderControl) {
            this.voxelMaterialManager = voxelMaterialManager;
            this.voxelRenderControl = voxelRenderControl;
        }

        @Override
        public StaticVoxelRenderer build() {
            return new StaticVoxelRenderer(voxelMaterialManager, voxelRenderControl);
        }

        @Override
        public Class<StaticVoxelRenderer> getComponentType() {
            return StaticVoxelRenderer.class;
        }
    }

}
