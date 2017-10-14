package nl.civcraft.core.tasks;


import nl.civcraft.core.gamecomponents.InventoryComponent;
import nl.civcraft.core.gamecomponents.Placable;
import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;

import java.util.Optional;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class PlaceBlock extends Task {

    private final PrefabManager stockpileManager;
    private final PrefabManager blockManager;
    private final String itemName;
    private final Matrix4f locationToPlace;
    private FetchFromStockpile fetchFromStockpile;
    private MoveToRange moveToRange;

    public PlaceBlock(PrefabManager stockpileManager,
                      PrefabManager blockManager,
                      String itemName,
                      Matrix4f locationToPlace) {
        super(State.TODO);
        this.stockpileManager = stockpileManager;
        this.blockManager = blockManager;
        this.itemName = itemName;
        this.locationToPlace = locationToPlace;
    }

    @Override
    public Result affect(GameObject target,
                         float tpf) {
        if (fetchFromStockpile == null) {
            fetchFromStockpile = new FetchFromStockpile(stockpileManager, itemName);
        } else if (!fetchFromStockpile.getState().equals(State.DONE)) {
            Result affect = fetchFromStockpile.affect(target, tpf);
            if (!affect.equals(Result.COMPLETED)) {
                return affect;
            }
        } else if (moveToRange == null) {
            Optional<GameObject> closestGameObject = blockManager.getClosestGameObject(locationToPlace, Voxel.class);
            if (closestGameObject.isPresent()) {
                moveToRange = new MoveToRange(closestGameObject.get(), 3.0f);
            } else {
                return Result.FAILED;
            }
        } else {
            if (!moveToRange.getState().equals(State.DONE)) {
                Result affect = moveToRange.affect(target, tpf);
                if (affect.equals(Result.COMPLETED)) {
                    GameObject gameObject = target.getComponent(InventoryComponent.class).orElseThrow(() -> new IllegalStateException("No inventory"))
                            .removeItem(itemName).orElseThrow(() -> new IllegalStateException("No item"));
                    gameObject.getComponent(Placable.class).orElseThrow(() -> new IllegalStateException("Not placable")).place(locationToPlace);
                    return Result.COMPLETED;
                }
                return affect;
            }
        }
        return Result.IN_PROGRESS;
    }

    public Matrix4f getLocationToPlace() {
        return locationToPlace;
    }
}
