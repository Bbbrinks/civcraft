package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.npc.Civvy;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Bob on 11-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class AStarPathFinder {

    public Queue<Voxel> findPath(Civvy civvy, Voxel start, Voxel target) {
        if (start.equals(target)) {
            return new LinkedBlockingDeque<>();
        }

        Set<AStarNode> openList = new HashSet<>();
        openList.add(new AStarNode(start));
        Set<AStarNode> closedList = new HashSet<>();
        AStarNode current = null;
        boolean done = false;
        while (!done) {
            current = findLowestCost(openList, target);
            closedList.add(current);
            openList.remove(current);
            for (Voxel voxel : current.getVoxel().getEnterableNeighbours()) {
                Optional<AStarNode> currentAdjecent = openList.stream().filter(n -> n.getVoxel().equals(voxel)).findFirst();
                if (!currentAdjecent.isPresent()) {
                    AStarNode neighbour = new AStarNode(voxel);
                    neighbour.setPrevious(current);
                    neighbour.sethCost(calculateCost(neighbour, target));
                    neighbour.setgCost(current);
                    openList.add(neighbour);
                } else {
                    AStarNode aStarNode = currentAdjecent.get();
                    if (aStarNode.getgCost() > aStarNode.calcGCost(current)) {
                        aStarNode.setPrevious(current);
                        aStarNode.setgCost(current);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new LinkedBlockingDeque<>();
            }
            done = target.equals(current.getVoxel());
        }
        return buildPath(current);
    }

    private AStarNode findLowestCost(Set<AStarNode> openList, Voxel target) {
        return openList.stream().min((v1, v2) -> calculateCost(v1, target) - calculateCost(v2, target)).get();
    }

    private int calculateCost(AStarNode next, Voxel target) {
        int xCost = Math.abs(next.getVoxel().getX() - target.getX());
        int yCost = Math.abs(next.getVoxel().getY() - target.getY());
        int zCost = Math.abs(next.getVoxel().getZ() - target.getZ());
        return xCost + yCost + zCost;
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
