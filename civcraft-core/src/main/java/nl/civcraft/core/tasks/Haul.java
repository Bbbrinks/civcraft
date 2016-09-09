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
    private final MoveTo moveToObject;
    private final MoveTo moveToStockPile;
    private final GameObject itemToHaul;
    private final Stockpile target;
    private boolean itemPickedUp = false;
    private Item item;

    public Haul(Stockpile target, GameObject itemToHaul, AStarPathFinder pathFinder) {
        super(State.TODO);
        this.itemToHaul = itemToHaul;
        moveToObject = new MoveTo(itemToHaul.getComponent(Physical.class).get().getCurrentVoxel(), pathFinder);
        Optional<Voxel> targetVoxel = target.getAvailableSpot(itemToHaul.getComponent(ItemComponent.class).get().getItem());
        this.target = target;
        if (!targetVoxel.isPresent()) {
            setState(State.FAILED);
            moveToStockPile = null;
            return;
        }
        moveToStockPile = new MoveTo(targetVoxel.get(), pathFinder);

    }

    @Override
    public Result affect(Civvy civvy, float tpf) {
        if (!moveToObject.getState().equals(State.DONE)) {
            Result affect = moveToObject.affect(civvy, tpf);
            if (affect.equals(Result.FAILED)) {
                return Result.FAILED;
            } else if (affect.equals(Result.COMPLETED)) {
                moveToObject.setState(State.DONE);
            }
            return Result.IN_PROGRESS;
        } else if (!itemPickedUp) {
            item = itemToHaul.getComponent(ItemComponent.class).get().getItem();
            civvy.getComponent(Inventory.class).get().addItem(item);
            itemToHaul.destroy();
            itemPickedUp = true;
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
}
