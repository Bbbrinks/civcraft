package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.utils.RandomUtil;

import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Wander extends Task {
    private final AStarPathFinder pathFinder;

    public Wander(AStarPathFinder pathFinder) {
        super(State.CONTINUAL);
        this.pathFinder = pathFinder;
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        Optional<GroundMovement> component = civvy.getComponent(GroundMovement.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Move to can only be done by GroundMovement game objects");
        }
        GroundMovement groundMovement = component.get();
        Voxel voxel = groundMovement.currentVoxel();
        if (voxel != null) {
            List<Voxel> possibleNextVoxels = voxel.getEnterableNeighbours();
            if (!possibleNextVoxels.isEmpty()) {
                Voxel target = possibleNextVoxels.get(RandomUtil.getNextInt(possibleNextVoxels.size()));
                MoveTo task = new MoveTo(target, pathFinder);
                task.setState(State.DOING);
                civvy.getComponent(Civvy.class).get().setTask(task);
            }
        }
        return Result.IN_PROGRESS;
    }
}
