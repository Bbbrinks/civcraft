package nl.civcraft.core.pathfinding;

import nl.civcraft.core.gamecomponents.Neighbour;
import nl.civcraft.core.model.GameObject;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class AStarPathFinder {

    public Queue<GameObject> findPath(GameObject civvy, GameObject start, PathFindingTarget target) {
        if (start.equals(target)) {
            return new LinkedBlockingDeque<>();
        }

        Set<AStarNode> openList = new HashSet<>();
        openList.add(new AStarNode(start));
        Set<AStarNode> closedList = new HashSet<>();
        AStarNode current = null;
        boolean done = false;
        int maxSearchArea = target.getMaxSearchArea(start);
        while (!done && closedList.size() < maxSearchArea) {
            current = findLowestCost(openList, target);
            closedList.add(current);
            openList.remove(current);
            for (GameObject voxel : current.getGameObject().getComponent(Neighbour.class).
                    map(Neighbour::getEnterableNeighbours).
                    orElse(Collections.emptyList())) {
                Optional<AStarNode> currentAdjacent = openList.stream().filter(n -> n.getGameObject().equals(voxel)).findFirst();
                if (!currentAdjacent.isPresent()) {
                    if (closedList.stream().anyMatch(n -> n.getGameObject().equals(voxel))) {
                        continue;
                    }
                    AStarNode neighbour = new AStarNode(voxel);
                    neighbour.setPrevious(current);
                    neighbour.sethCost(calculateCost(neighbour, target));
                    neighbour.setgCost(current);
                    openList.add(neighbour);
                } else {
                    AStarNode aStarNode = currentAdjacent.get();
                    if (aStarNode.getgCost() > aStarNode.calcGCost(current)) {
                        aStarNode.setPrevious(current);
                        aStarNode.setgCost(current);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new LinkedBlockingDeque<>();
            }
            done = target.isReached(civvy, current);
        }
        if (done) {
            return buildPath(current);
        } else {
            return null;
        }
    }

    private AStarNode findLowestCost(Set<AStarNode> openList, PathFindingTarget target) {
        return openList.stream().min(Comparator.comparingInt(v -> calculateCost(v, target))).map(n -> n).orElseThrow(() -> new IllegalStateException("No lowest cost node"));
    }

    private int calculateCost(AStarNode next, PathFindingTarget target) {
        return target.getCostFrom(next);

    }

    private Queue<GameObject> buildPath(AStarNode current) {
        List<GameObject> path = new ArrayList<>();
        path.add(current.getGameObject());
        while (current.getPrevious() != null) {
            current = current.getPrevious();
            path.add(current.getGameObject());
        }
        Collections.reverse(path);
        Queue<GameObject> reversed = new LinkedBlockingDeque<>();
        reversed.addAll(path);
        return reversed;
    }

    private void expandSearchArea(Set<GameObject> openList, Set<GameObject> closedList) {
        List<GameObject> newNeighbours = new ArrayList<>();
        for (GameObject voxel : openList) {
            if (!closedList.contains(voxel)) {
                newNeighbours.addAll(voxel.getComponent(Neighbour.class).map(Neighbour::getEnterableNeighbours).orElse(Collections.emptyList()));
                closedList.add(voxel);
            }
        }
        openList.addAll(newNeighbours);
    }
}
