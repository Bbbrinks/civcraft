package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import nl.civcraft.core.gamecomponents.*;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TickManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;

import javax.inject.Named;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class PrefabsModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Named("civvy")
    public PrefabManager civvyManager(
            VoxelManager voxelManager,
            AStarPathFinder aStarPathFinder,
            TickManager tickManager) {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new InventoryComponent.Factory(2));
        prefabManager.registerComponent(new Civvy.Factory());
        prefabManager.registerComponent(new GroundMovement.Factory(2f, voxelManager, aStarPathFinder));
        prefabManager.registerComponent(new Hunger.Factory(tickManager));
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("item")
    public PrefabManager itemManager() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new Haulable.Factory());
        return prefabManager;
    }


    @Provides
    @Singleton
    @Named("block")
    public PrefabManager blockManager() {
        return new PrefabManager();
    }

    @Provides
    @Singleton
    @Named("stockpile")
    public PrefabManager stockpile(
            Stockpile.Factory stockpileComponent) {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(stockpileComponent);
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("voxelHighlight")
    public PrefabManager voxelHighlight() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new VoxelHighlight.Factory());
        return prefabManager;
    }

    @Provides
    @Singleton
    @Named("planningGhost")
    public PrefabManager planningGhost() {
        PrefabManager prefabManager = new PrefabManager();
        prefabManager.registerComponent(new PlanningGhost.Factory());
        return prefabManager;
    }
}
