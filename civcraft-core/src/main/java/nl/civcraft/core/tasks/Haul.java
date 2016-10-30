package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.managers.Physical;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;

import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Haul extends Task {
    private final GameObject itemToHaul;
    private final Stockpile target;
    private final AStarPathFinder pathFinder;
    private MoveTo moveToObject;
    private MoveTo moveToStockPile;
    private boolean itemPickedUp = false;
    private Item item;

    public Haul(Stockpile target, GameObject itemToHaul, AStarPathFinder pathFinder) {
        super(State.TODO);
        this.itemToHaul = itemToHaul;
        this.pathFinder = pathFinder;
        this.target = target;
    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        if (moveToObject == null) {
            moveToObject = new MoveTo(itemToHaul.getComponent(Physical.class).get().getCurrentVoxel(), pathFinder);
        }
        if (!moveToObject.getState().equals(State.DONE)) {
            return moveToObject(civvy, tpf);
        } else if (!itemPickedUp) {
            item = itemToHaul.getComponent(ItemComponent.class).get().getItem();
            civvy.getComponent(Inventory.class).get().addItem(item);
            itemToHaul.destroy();
            itemPickedUp = true;

            Optional<Voxel> targetVoxel = target.getAvailableSpot(itemToHaul.getComponent(ItemComponent.class).get().getItem());
            if (!targetVoxel.isPresent()) {
                setState(State.FAILED);
                moveToStockPile = null;
                return Result.FAILED;
            }
            moveToStockPile = new MoveTo(targetVoxel.get(), pathFinder);
            return Result.IN_PROGRESS;
        } else if (!moveToStockPile.getState().equals(State.DONE)) {
            Result affect = moveToStockPile.affect(civvy, tpf);
            if (affect.equals(Result.FAILED)) {
                return Result.FAILED;
            } else if (affect.equals(Result.COMPLETED)) {
                moveToStockPile.setState(State.DONE);
            }
            return Result.IN_PROGRESS;
        } else {
            civvy.getComponent(Inventory.class).get().remove(item);
            target.addItem(item);
            return Result.COMPLETED;
        }
    }

    private Result moveToObject(Civvy civvy, float tpf) {
        Result affect = moveToObject.affect(civvy, tpf);
        if (affect.equals(Result.FAILED)) {
            return Result.FAILED;
        } else if (affect.equals(Result.COMPLETED)) {
            moveToObject.setState(State.DONE);
        }
        return Result.IN_PROGRESS;
    }
}
