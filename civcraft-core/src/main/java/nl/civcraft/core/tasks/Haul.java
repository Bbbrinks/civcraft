package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.Inventory;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
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
    private final VoxelManager voxelManager;
    private MoveToRange moveToObject;
    private MoveTo moveToStockPile;
    private boolean itemPickedUp = false;

    public Haul(Stockpile target, GameObject itemToHaul, AStarPathFinder pathFinder, VoxelManager world) {
        super(State.TODO);
        this.itemToHaul = itemToHaul;
        this.pathFinder = pathFinder;
        this.target = target;
        this.voxelManager = world;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        if (moveToObject == null) {
            moveToObject = new MoveToRange(itemToHaul, 3.0f, pathFinder);
        }
        if (!moveToObject.getState().equals(State.DONE)) {
            return moveToObject(civvy, tpf);
        } else {
            Optional<Inventory> civvyInventory = civvy.getComponent(Inventory.class);
            if (!civvyInventory.isPresent()) {
                return Result.FAILED;
            }
            if (!itemPickedUp) {
                itemPickedUp = civvyInventory.get().addItem(itemToHaul);

                Optional<GameObject> targetVoxel = target.getAvailableSpot(itemToHaul);
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
                civvyInventory.get().remove(itemToHaul);
                target.addItem(itemToHaul);
                return Result.COMPLETED;
            }
        }
    }

    @Override
    public boolean canBeHandledBy(Civvy civvy) {
        return true;
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
