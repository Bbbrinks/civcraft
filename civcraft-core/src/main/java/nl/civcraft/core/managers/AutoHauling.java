package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.model.events.GameObjectChangedEvent;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class AutoHauling {
    private final TaskManager taskManager;
    private final PrefabManager stockpileManager;
    private final Queue<Haulable> notYetHauled;

    @Autowired
    public AutoHauling(TaskManager taskManager, @Qualifier("stockpile") PrefabManager stockpileManager) {
        this.taskManager = taskManager;
        this.stockpileManager = stockpileManager;
        notYetHauled = new LinkedBlockingQueue<>();
    }

    @EventListener
    public void handleEntityCreated(GameObjectCreatedEvent gameObjectCreatedEvent) {
        Optional<Haulable> haulable = gameObjectCreatedEvent.getGameObject().getComponent(Haulable.class);
        haulable.ifPresent(this::handleNewHaulable);
        gameObjectCreatedEvent.getGameObject().getComponent(Stockpile.class).ifPresent(this::handleNewStockpile);
    }

    private void handleNewStockpile(Stockpile stockpile) {
        while (!notYetHauled.isEmpty()) {
            Haulable poll = notYetHauled.poll();
            if (poll.shouldBeHauled()) {
                taskManager.addTask(poll.getTask(stockpile));
            }
        }
    }

    @EventListener
    public void handleEntityUpdated(GameObjectChangedEvent gameObjectChangedEvent) {
        Optional<Haulable> haulable = gameObjectChangedEvent.getGameObject().getComponent(Haulable.class);
        haulable.ifPresent(this::handleNewHaulable);
        gameObjectChangedEvent.getGameObject().getComponent(Stockpile.class).ifPresent(this::handleNewStockpile);
    }

    private void handleNewHaulable(Haulable haulable) {
        if (haulable.shouldBeHauled()) {
            Optional<Stockpile> stockPile = stockpileManager.getClosestGameObject(haulable.getGameObject().getTransform(), Stockpile.class).
                    map(o -> o.getComponent(Stockpile.class).
                            orElseThrow(() -> new IllegalStateException("Found stockipile without stockpile component")));
            if (stockPile.isPresent()) {
                taskManager.addTask(haulable.getTask(stockPile.get()));
            } else {
                notYetHauled.add(haulable);
            }
        }
    }
}
