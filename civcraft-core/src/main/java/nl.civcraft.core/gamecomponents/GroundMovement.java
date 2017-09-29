package nl.civcraft.core.gamecomponents;

import io.reactivex.disposables.Disposable;
import nl.civcraft.core.managers.TickManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.pathfinding.AStarNode;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.pathfinding.ChangeAwarePath;
import nl.civcraft.core.pathfinding.PathFindingTarget;
import nl.civcraft.core.pathfinding.exceptions.UnreachableVoxelException;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * Created by Bob on 18-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class GroundMovement extends AbstractGameComponent {
    private final float speed;
    private final VoxelManager voxelManager;
    private final AStarPathFinder aStarPathFinder;
    private GameObject currentVoxel;
    private Disposable currentVoxelSubscription;
    private PathFindingTarget currentTarget;
    private ChangeAwarePath currentPath;

    public GroundMovement(float speed,
                          VoxelManager voxelManager,
                          AStarPathFinder aStarPathFinder,
                          TickManager tickManager) {
        // Divide speed  so it's 1 block/sec
        this.speed = speed / 20;
        this.voxelManager = voxelManager;
        this.aStarPathFinder = aStarPathFinder;
        tickManager.getTick().subscribe(this::handleTick);
        currentPath = null;
    }

    private void handleTick(Long tickLenght) {
        if (currentPath == null) {
            return;
        }
        Optional<Queue<GameObject>> currentPathOptional = currentPath.getCurrentPath();
        if (!currentPathOptional.isPresent()) {
            this.currentTarget = null;
            return;
        }
        Queue<GameObject> path = currentPathOptional.get();

        GameObject nextTarget = path.peek();
        if (nextTarget != null) {
            Vector3f location = nextTarget.getTransform().getTranslation(new Vector3f()).add(new Vector3f(0, 1, 0));
            Vector3f gameObjectTransform = gameObject.getTransform().getTranslation(new Vector3f());
            Vector3f movement = location.sub(gameObjectTransform);
            if (distance(nextTarget) >= speed) {
                movement.normalize();
                movement = movement.mul(speed);
            } else {
                setCurrentVoxel(nextTarget);
                path.poll();
            }
            gameObject.getTransform().setTranslation(gameObjectTransform.add(movement));
        } else {
            this.currentPath = null;
        }
    }

    private float distance(GameObject target) {
        return target.getTransform().getTranslation(new Vector3f()).distance(gameObject.getTransform().getTranslation(new Vector3f()).sub(0, 1, 0));
    }

    public boolean moveToward(PathFindingTarget target) throws UnreachableVoxelException {
        if (!Objects.equals(target, currentTarget)) {
            currentTarget = target;
            currentPath = new ChangeAwarePath(aStarPathFinder, currentTarget, getGameObject());
            if (!currentPath.getCurrentPath().isPresent()) {
                throw new UnreachableVoxelException();
            }
        }
        return currentTarget.isReached(getGameObject(), new AStarNode(getCurrentVoxel()));
    }

    public GameObject getCurrentVoxel() {
        if (currentVoxel == null) {
            setCurrentVoxelToGround(gameObject);
        }
        return currentVoxel;
    }

    private void setCurrentVoxelToGround(GameObject gameObject) {
        GameObject groundVoxel = voxelManager.getGroundAt(gameObject.getTransform().getTranslation(new Vector3f()), 10).
                orElseThrow(() -> new IllegalStateException("No ground found at " + gameObject.getTransform().getTranslation(new Vector3f())));

        setCurrentVoxel(groundVoxel);
    }

    private void setCurrentVoxel(GameObject currentVoxel) {
        if (currentVoxelSubscription != null && !currentVoxelSubscription.isDisposed()) {
            currentVoxelSubscription.dispose();
        }

        currentVoxelSubscription = currentVoxel.getGameObjectDestroyed().subscribe(gameObject1 ->
        {
            currentVoxelSubscription.dispose();
            this.currentVoxel = null;
        });

        this.currentVoxel = currentVoxel;
        float newY = currentVoxel.getTransform().getTranslation(new Vector3f()).y() + 1;
        getGameObject().getTransform().getTranslation(new Vector3f());
    }

    public static class Factory implements GameComponentFactory<GroundMovement> {
        private final float speed;
        private final VoxelManager voxelManager;
        private final AStarPathFinder aStarPathFinder;
        private final TickManager tickManager;

        @SuppressWarnings("SameParameterValue")
        public Factory(float speed,
                       VoxelManager voxelManager,
                       AStarPathFinder aStarPathFinder,
                       TickManager tickManager) {
            this.speed = speed;
            this.voxelManager = voxelManager;
            this.aStarPathFinder = aStarPathFinder;
            this.tickManager = tickManager;
        }

        @Override
        public GroundMovement build() {
            return new GroundMovement(speed, voxelManager, aStarPathFinder, tickManager);
        }

        @Override
        public Class<GroundMovement> getComponentType() {
            return GroundMovement.class;
        }
    }
}
