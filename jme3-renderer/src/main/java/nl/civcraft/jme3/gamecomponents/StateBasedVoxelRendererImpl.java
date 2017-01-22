package nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.jme3.model.RenderedVoxelFace;
import nl.civcraft.jme3.rendering.VoxelMaterialManager;
import nl.civcraft.jme3.utils.BlockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private StateBasedVoxelRendererImpl(VoxelMaterialManager voxelMaterialManager, Function<GameObject, String> stateSupplier) {
        this.voxelMaterialManager = voxelMaterialManager;
        this.stateSupplier = stateSupplier;
    }

    @Override
    public Map<Face, RenderedVoxelFace> getFaces() {
        return BlockUtil.getQuadBlock(voxelMaterialManager.loadMaterial(voxel.getType(), stateSupplier.apply(gameObject)));
    }

    @Component
    public static class StateBasedVoxelRendererFactoryImpl implements nl.civcraft.core.rendering.VoxelRenderer.StateBasedVoxelRendererFactoryFactory<StateBasedVoxelRendererImpl> {


        private final VoxelMaterialManager voxelMaterialManager1;

        @Autowired
        public StateBasedVoxelRendererFactoryImpl(VoxelMaterialManager voxelMaterialManager1) {
            this.voxelMaterialManager1 = voxelMaterialManager1;
        }

        @Override
        public GameComponentFactory<StateBasedVoxelRendererImpl> build(Function<GameObject, String> stateSupplier) {
            return new GameComponentFactory<StateBasedVoxelRendererImpl>() {
                @Override
                public StateBasedVoxelRendererImpl build() {
                    return new StateBasedVoxelRendererImpl(voxelMaterialManager1, stateSupplier);
                }

                @Override
                public Class<StateBasedVoxelRendererImpl> getComponentType() {
                    return StateBasedVoxelRendererImpl.class;
                }
            };
        }
    }
}
