package nl.civcraft.core.pathfinding;

import io.reactivex.disposables.Disposable;
import nl.civcraft.core.gamecomponents.GroundMovement;
import nl.civcraft.core.model.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

/**
 * Created by Bob on 29-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class ChangeAwarePath {

    private final AStarPathFinder pathFinder;
    private final PathFindingTarget target;
    private final GameObject subject;
    private final GroundMovement groundMovement;
    private final Map<GameObject, Disposable> subscriptions;
    private Queue<GameObject> currentPath;

    public ChangeAwarePath(AStarPathFinder pathFinder,
                           PathFindingTarget target,
                           GameObject subject) {

        this.groundMovement = subject.getComponent(GroundMovement.class).orElseThrow(() -> new IllegalStateException("Only ground movement objects can use pathfinding"));
        this.pathFinder = pathFinder;
        this.target = target;
        this.subject = subject;
        subscriptions = new HashMap<>();
    }

    public Optional<Queue<GameObject>> getCurrentPath() {
        if (currentPath != null) {
            return Optional.of(currentPath);
        }
        Optional<Queue<GameObject>> path = pathFinder.findPath(subject, groundMovement.getCurrentVoxel(), target);
        if (path.isPresent()) {
            currentPath = path.get();
            for (GameObject gameObject : currentPath) {
                Disposable subscribe = gameObject.getGameObjectDestroyed().subscribe(gameObject1 -> clearPath());
                subscriptions.put(gameObject, subscribe);
            }
        }
        return path;

    }

    private void clearPath() {
        subscriptions.values().forEach(Disposable::dispose);
        subscriptions.clear();
        currentPath = null;
    }

}
