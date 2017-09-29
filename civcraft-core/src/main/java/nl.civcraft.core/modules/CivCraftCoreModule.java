package nl.civcraft.core.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Names;
import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import nl.civcraft.core.SystemEventPublisher;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.selectors.SingleVoxelSelector;
import nl.civcraft.core.interaction.tools.*;
import nl.civcraft.core.managers.AutoHauling;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Task;
import nl.civcraft.core.tasks.Wander;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftCoreModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PrefabsModule());
        install(new BlocksModule());
        install(new ItemsModule());
        install(new WorldGenerationModule());

        bind(Scheduler.class).to(SingleScheduler.class).in(Singleton.class);
        bind(SystemEventPublisher.class).asEagerSingleton();


        bind(MouseTool.class).annotatedWith(Names.named("singleVoxelSelector")).to(SingleVoxelSelector.class).in(Singleton.class);
        bind(MouseTool.class).annotatedWith(Names.named("stockpileTool")).to(StockpileTool.class).in(Singleton.class);
        bind(MouseTool.class).annotatedWith(Names.named("breakBlockTool")).to(BreakBlockTool.class).in(Singleton.class);
        bind(MouseTool.class).annotatedWith(Names.named("harvestTool")).to(HarvestTool.class).in(Singleton.class);
        bind(MouseTool.class).annotatedWith(Names.named("buildWallTool")).to(BuildWallTool.class).in(Singleton.class);
        bind(MouseTool.class).annotatedWith(Names.named("levelGroundTool")).to(LevelGroundTool.class).in(Singleton.class);

        Multibinder<MouseTool> toolBinder = Multibinder.newSetBinder(binder(), MouseTool.class);
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("singleVoxelSelector")));
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("stockpileTool")));
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("breakBlockTool")));
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("harvestTool")));
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("buildWallTool")));
        toolBinder.addBinding().to(Key.get(MouseTool.class, Names.named("levelGroundTool")));

        Multibinder.newSetBinder(binder(), Task.class).addBinding().to(Wander.class).in(Singleton.class);

        bind(TaskManager.class).asEagerSingleton();
        bind(AStarPathFinder.class).asEagerSingleton();
        bind(VoxelManager.class).asEagerSingleton();
        bind(AutoHauling.class).asEagerSingleton();


    }

}

