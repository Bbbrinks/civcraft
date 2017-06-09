package nl.civcraft.core.gamecomponents;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelHighlight extends AbstractGameComponent {
    public static class Factory implements GameComponentFactory<VoxelHighlight> {

        @Override
        public VoxelHighlight build() {
            return new VoxelHighlight();
        }

        @Override
        public Class<VoxelHighlight> getComponentType() {
            return VoxelHighlight.class;
        }
    }
}
