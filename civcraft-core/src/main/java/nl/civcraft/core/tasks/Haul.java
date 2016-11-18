package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.*;
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
    private final World world;
    private MoveTo moveToObject;
    private MoveTo moveToStockPile;
    private boolean itemPickedUp = false;
    private Item item;

    public Haul(Stockpile target, GameObject itemToHaul, AStarPathFinder pathFinder, World world) {
        super(State.TODO);
        this.itemToHaul = itemToHaul;
        this.pathFinder = pathFinder;
        this.target = target;
        this.world = world;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        if (moveToObject == null) {
            moveToObject = new MoveTo(world.getGroundAt(itemToHaul.getTransform().getTranslation(), 10).get(), pathFinder);
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

    private Result moveToObject(GameObject civvy, float tpf) {
        Result affect = moveToObject.affect(civvy, tpf);
        if (affect.equals(Result.FAILED)) {
            return Result.FAILED;
        } else if (affect.equals(Result.COMPLETED)) {
            moveToObject.setState(State.DONE);
        }
        return Result.IN_PROGRESS;
    }
}
