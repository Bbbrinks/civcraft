package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.model.RenderedVoxelFace;
import nl.civcraft.jme3.rendering.VoxelMaterialManager;
import nl.civcraft.jme3.rendering.VoxelRendererControl;
import nl.civcraft.jme3.utils.BlockUtil;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StateBasedVoxelRendererImpl extends VoxelRenderer {

    private final VoxelMaterialManager voxelMaterialManager;
    private final Function<GameObject, String> stateSupplier;

    private StateBasedVoxelRendererImpl(VoxelMaterialManager voxelMaterialManager, Function<GameObject, String> stateSupplier, VoxelRendererControl voxelRendererControl) {
        super(voxelRendererControl);
        this.voxelMaterialManager = voxelMaterialManager;
        this.stateSupplier = stateSupplier;
    }

    @Override
    public Map<Face, RenderedVoxelFace> getFaces() {
        return BlockUtil.getQuadBlock(voxelMaterialManager.loadMaterial(getVoxel().getType(), stateSupplier.apply(gameObject)));
    }

    public static class StateBasedVoxelRendererFactoryImpl implements nl.civcraft.core.rendering.VoxelRenderer.StateBasedVoxelRendererFactoryFactory<StateBasedVoxelRendererImpl> {


        private final VoxelMaterialManager voxelMaterialManager1;
        private final VoxelRendererControl voxelRendererControl;

        @Inject
        public StateBasedVoxelRendererFactoryImpl(VoxelMaterialManager voxelMaterialManager1, VoxelRendererControl voxelRendererControl) {
            this.voxelMaterialManager1 = voxelMaterialManager1;
            this.voxelRendererControl = voxelRendererControl;
        }

        @Override
        public StaticVoxelRendererFactory<StateBasedVoxelRendererImpl> build(Function<GameObject, String> stateSupplier) {
            return new StaticVoxelRendererFactory<StateBasedVoxelRendererImpl>() {
                @Override
                public StateBasedVoxelRendererImpl build() {
                    return new StateBasedVoxelRendererImpl(voxelMaterialManager1, stateSupplier, voxelRendererControl);
                }

                @Override
                public Class<StateBasedVoxelRendererImpl> getComponentType() {
                    return StateBasedVoxelRendererImpl.class;
                }
            };
        }
    }
}
