package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.Haulable;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public AutoHauling(TaskManager taskManager,
                       @Qualifier("stockpile") PrefabManager stockpileManager,
                       @Qualifier("item") PrefabManager itemManager) {

        itemManager.getGameObjectCreated().subscribe(this::handleNewItem);
        stockpileManager.getGameObjectCreated().subscribe(this::handleNewStockpile);

        itemManager.getGameObjectChangedEvent().subscribe(this::handleNewItem);
        stockpileManager.getGameObjectChangedEvent().subscribe(this::handleNewStockpile);

        this.taskManager = taskManager;
        this.stockpileManager = stockpileManager;
        notYetHauled = new LinkedBlockingQueue<>();
    }

    public void handleNewItem(GameObject gameObject) {
        Haulable haulable = gameObject.getComponent(Haulable.class).orElseThrow(() -> new IllegalStateException("Items are not haulable"));
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


    private void handleNewStockpile(GameObject gameObject) {
        Stockpile stockpile = gameObject.getComponent(Stockpile.class).orElseThrow(() -> new IllegalStateException("Stockpiles are not stockpiles"));
        while (!notYetHauled.isEmpty()) {
            Haulable poll = notYetHauled.poll();
            if (poll.shouldBeHauled()) {
                taskManager.addTask(poll.getTask(stockpile));
            }
        }
    }
}
