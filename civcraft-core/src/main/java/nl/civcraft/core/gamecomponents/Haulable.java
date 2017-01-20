package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.tasks.Haul;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class Haulable extends AbstractGameComponent {

    public Task getTask(Stockpile target, AStarPathFinder pathFinder, VoxelManager voxelManager) {
        return new Haul(target, gameObject, pathFinder, voxelManager);
    }

    public static class Factory implements GameComponentFactory<Haulable> {
        @Override
        public Haulable build() {
            return new Haulable();
        }

        @Override
        public Class<Haulable> getComponentType() {
            return Haulable.class;
        }
    }
}
