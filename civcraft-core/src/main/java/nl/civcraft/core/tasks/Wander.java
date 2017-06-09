package nl.civcraft.core.tasks;

import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.utils.RandomUtil;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Wander extends Task {

    public Wander() {
        super(State.CONTINUAL);
    }

    @Override
    public Result affect(GameObject civvy, float tpf) {
        Optional<GroundMovement> component = civvy.getComponent(GroundMovement.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("Move to can only be done by GroundMovement game objects");
        }
        GroundMovement groundMovement = component.get();
        GameObject currentVoxel = groundMovement.currentVoxel();
        if (currentVoxel != null) {
            List<GameObject> possibleNextVoxels = currentVoxel.getComponent(Neighbour.class).map(Neighbour::getEnterableNeighbours).orElse(Collections.emptyList());
            if (!possibleNextVoxels.isEmpty()) {
                GameObject target = possibleNextVoxels.get(RandomUtil.getNextInt(possibleNextVoxels.size()));
                MoveTo task = new MoveTo(target);
                task.setState(State.DOING);

                Optional<Civvy> civvyOptional = civvy.getComponent(Civvy.class);
                if (civvyOptional.isPresent()) {
                    civvyOptional.get().setTask(task);
                } else {
                    throw new IllegalStateException("Only civvies can wander");
                }
            }
        }
        return Result.IN_PROGRESS;
    }
}
