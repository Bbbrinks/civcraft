package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.Voxel;

/**
 * Created by Bob on 11-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class AStarNode {
    private final Voxel voxel;
    private AStarNode previous;
    private int hCost;
    private int gCost;

    public AStarNode(Voxel voxel) {
        this.voxel = voxel;
    }

    public Voxel getVoxel() {
        return voxel;
    }

    public AStarNode getPrevious() {
        return previous;
    }

    public void setPrevious(AStarNode previous) {
        this.previous = previous;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int getgCost() {
        return gCost;
    }


    public int calcGCost(AStarNode current) {
        return current.getgCost() + hCost;
    }

    public void setgCost(AStarNode current) {
        this.gCost = calcGCost(current);
    }
}
