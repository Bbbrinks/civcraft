package nl.civcraft.core.rendering;

import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.model.GameObject;

import java.util.function.Function;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface VoxelRenderer extends GameComponent {

    interface StaticVoxelRendererFactory<T extends VoxelRenderer> extends GameComponentFactory<T> {
    }

    @FunctionalInterface
    interface StateBasedVoxelRendererFactoryFactory<T extends VoxelRenderer> {
        GameComponentFactory<T> build(Function<GameObject, String> stateSupplier);
    }
}
