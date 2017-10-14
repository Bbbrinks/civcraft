package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.InventoryComponent;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class FetchFromStockpile extends Task {

    private final PrefabManager stockpileManager;
    private final String itemName;
    private Stockpile targetStockpile;
    private MoveToRange moveToRange;

    FetchFromStockpile(PrefabManager stockpileManager,
                       String itemName) {
        super(State.TODO);
        this.stockpileManager = stockpileManager;
        this.itemName = itemName;
    }

    @Override
    public Result affect(GameObject target,
                         float tpf) {
        if (targetStockpile == null || !targetStockpile.hasItem(itemName)) {
            Optional<Stockpile> closestGameObject = stockpileManager.getClosestGameObject(target.getTransform(), gameObject ->
                    gameObject.getComponent(Stockpile.class)
                            .map(stockpile -> stockpile.hasItem(itemName))
                            .orElse(false))
                    .map(gameObject -> gameObject.getComponent(Stockpile.class).orElse(null));
            if (!closestGameObject.isPresent()) {
                return Result.FAILED;
            } else {
                targetStockpile = closestGameObject.get();
            }
        } else if (moveToRange == null) {
            moveToRange = new MoveToRange(targetStockpile.getSpotWithItem(itemName).orElseThrow(() -> new IllegalStateException("Item disappeared")), 3.0f);
        } else {
            Result affect = moveToRange.affect(target, tpf);
            if (affect.equals(Result.COMPLETED)) {
                target.getComponent(InventoryComponent.class).orElseThrow(() -> new IllegalStateException("No inventory"))
                        .addItem(targetStockpile.removeItem(itemName).orElseThrow(() -> new IllegalStateException("Item disappeared")));
                return Result.COMPLETED;
            } else {
                return affect;
            }
        }
        return Result.IN_PROGRESS;

    }

}
