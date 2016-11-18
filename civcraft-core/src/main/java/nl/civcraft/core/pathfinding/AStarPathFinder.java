package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Voxel;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class AStarPathFinder {

    public Queue<Voxel> findPath(GameObject civvy, Voxel start, PathFindingTarget target) {
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
            for (Voxel voxel : current.getVoxel().getEnterableNeighbours()) {
                Optional<AStarNode> currentAdjacent = openList.stream().filter(n -> n.getVoxel().equals(voxel)).findFirst();
                if (!currentAdjacent.isPresent()) {
                    if (closedList.stream().filter(n -> n.getVoxel().equals(voxel)).findFirst().isPresent()) {
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
        return openList.stream().min((v1, v2) -> calculateCost(v1, target) - calculateCost(v2, target)).get();
    }

    private int calculateCost(AStarNode next, PathFindingTarget target) {
        return target.getCostFrom(next);

    }

    private Queue<Voxel> buildPath(AStarNode current) {
        List<Voxel> path = new ArrayList<>();
        path.add(current.getVoxel());
        while (current.getPrevious() != null) {
            current = current.getPrevious();
            path.add(current.getVoxel());
        }
        Collections.reverse(path);
        Queue<Voxel> reversed = new LinkedBlockingDeque<>();
        reversed.addAll(path);
        return reversed;
    }

    private void expandSearchArea(Set<Voxel> openList, Set<Voxel> closedList) {
        List<Voxel> newNeighbours = new ArrayList<>();
        for (Voxel voxel : openList) {
            if (!closedList.contains(voxel)) {
                newNeighbours.addAll(voxel.getEnterableNeighbours());
                closedList.add(voxel);
            }
        }
        openList.addAll(newNeighbours);
    }
}
