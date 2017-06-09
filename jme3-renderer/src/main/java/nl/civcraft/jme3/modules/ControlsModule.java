package nl.civcraft.jme3.modules;

import com.google.inject.AbstractModule;
import nl.civcraft.jme3.input.CivCraftFlyCamState;
import nl.civcraft.jme3.input.FlyingCamera;
import nl.civcraft.jme3.input.GlobalInput;
import nl.civcraft.jme3.input.VoxelSelectionInput;
import nl.civcraft.jme3.rendering.*;
import nl.civcraft.jme3.worldgeneration.WorldGeneratorState;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class ControlsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(VoxelRendererControl.class).asEagerSingleton();
        bind(ItemRendererControl.class).asEagerSingleton();
        bind(CivvyControl.class).asEagerSingleton();
        bind(VoxelHighlightControl.class).asEagerSingleton();
        bind(SelectionControl.class).asEagerSingleton();
        bind(CivCraftFlyCamState.class).asEagerSingleton();
        bind(FlyingCamera.class).asEagerSingleton();
        bind(GlobalInput.class).asEagerSingleton();
        bind(VoxelSelectionInput.class).asEagerSingleton();
        bind(WorldGeneratorState.class).asEagerSingleton();
        bind(StockpileRenderer.class).asEagerSingleton();
    }
}
