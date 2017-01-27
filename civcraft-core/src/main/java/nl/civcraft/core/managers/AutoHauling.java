package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.model.events.GameObjectChangedEvent;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final TaskManager taskManager;
    private final AStarPathFinder pathFinder;
    private final VoxelManager voxelManager;
    private final PrefabManager stockpileManager;

    @Autowired
    public AutoHauling(TaskManager taskManager, AStarPathFinder pathFinder, VoxelManager voxelManager, @Qualifier("stockpile") PrefabManager stockpileManager) {
        this.taskManager = taskManager;
        this.pathFinder = pathFinder;
        this.voxelManager = voxelManager;
        this.stockpileManager = stockpileManager;
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
        if (haulable.shouldBeHauled()) {
            Optional<Stockpile> stockPile = stockpileManager.getClosestGameObject(haulable.getGameObject().getTransform(), Stockpile.class).
                    map(o -> o.getComponent(Stockpile.class).
                            orElseThrow(() -> new IllegalStateException("Found stockipile without stockpile component")));
            stockPile.ifPresent(stockpile -> taskManager.addTask(haulable.getTask(stockpile, pathFinder, voxelManager)));
        }
    }
}
