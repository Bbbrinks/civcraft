package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.World;
import nl.civcraft.core.model.events.GameObjectChangedEvent;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class AutoHauling {
    private final World world;
    private final TaskManager taskManager;
    private final AStarPathFinder pathFinder;
    private final VoxelManager voxelManager;

    @Autowired
    public AutoHauling(WorldManager worldManager, TaskManager taskManager, AStarPathFinder pathFinder, VoxelManager voxelManager) {
        this.world = worldManager.getWorld();
        this.taskManager = taskManager;
        this.pathFinder = pathFinder;
        this.voxelManager = voxelManager;
    }

    @EventListener
    public void handleEntityCreated(GameObjectCreatedEvent gameObjectCreatedEvent) {
        Optional<Haulable> haulable = gameObjectCreatedEvent.getGameObject().getComponent(Haulable.class);
        haulable.ifPresent(this::handleNewHaulable);
        //TODO: add auto haul after a stockpile is created
    }

    @EventListener
    public void handleEntityUpdated(GameObjectChangedEvent gameObjectChangedEvent) {
        Optional<Haulable> haulable = gameObjectChangedEvent.getGameObject().getComponent(Haulable.class);
        haulable.ifPresent(this::handleNewHaulable);
        //TODO: add auto haul after a stockpile is created

    }

    private void handleNewHaulable(Haulable haulable) {
        Optional<Stockpile> stockPile = world.getStockPile();
        stockPile.ifPresent(stockpile -> taskManager.addTask(haulable.getTask(stockpile, pathFinder, voxelManager)));
    }
}
