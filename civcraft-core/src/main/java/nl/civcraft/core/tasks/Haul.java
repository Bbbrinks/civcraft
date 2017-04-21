package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.InventoryComponent;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Haul extends Task {
    private final GameObject itemToHaul;
    private final Stockpile target;
    private MoveToRange moveToObject;
    private MoveTo moveToStockPile;
    private boolean itemPickedUp = false;

    public Haul(Stockpile target, GameObject itemToHaul) {
        super(State.TODO);
        this.itemToHaul = itemToHaul;
        this.target = target;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        if (moveToObject == null) {
            moveToObject = new MoveToRange(itemToHaul, 3.0f);
        }
        if (!moveToObject.getState().equals(State.DONE)) {
            return moveToObject(civvy, tpf);
        }
        Optional<InventoryComponent> civvyInventory = civvy.getComponent(InventoryComponent.class);
        if (!civvyInventory.isPresent()) {
            return Result.FAILED;
        }
        if (!itemPickedUp) {
            return pickUpItem(civvyInventory.get());
        } else if (!moveToStockPile.getState().equals(State.DONE)) {
            return moveToStockpile(civvy, tpf);
        } else {
            civvyInventory.get().remove(itemToHaul);
            target.addItem(itemToHaul);
            return Result.COMPLETED;
        }
    }

    private Result moveToObject(GameObject civvy, float tpf) {
        Result affect = moveToObject.affect(civvy, tpf);
        if (affect.equals(Result.FAILED)) {
            return Result.FAILED;
        } else if (affect.equals(Result.COMPLETED)) {
            moveToObject.setState(State.DONE);
        }
        return Result.IN_PROGRESS;
    }

    private Result pickUpItem(InventoryComponent civvyInventory) {
        itemPickedUp = civvyInventory.addItem(itemToHaul);

        Optional<GameObject> targetVoxel = target.getAvailableSpot(itemToHaul);
        if (!targetVoxel.isPresent()) {
            setState(State.FAILED);
            moveToStockPile = null;
            return Result.FAILED;
        }
        moveToStockPile = new MoveTo(targetVoxel.get());
        return Result.IN_PROGRESS;
    }

    private Result moveToStockpile(GameObject civvy, float tpf) {
        Result affect = moveToStockPile.affect(civvy, tpf);
        if (affect.equals(Result.FAILED)) {
            return Result.FAILED;
        } else if (affect.equals(Result.COMPLETED)) {
            moveToStockPile.setState(State.DONE);
        }
        return Result.IN_PROGRESS;
    }
}
