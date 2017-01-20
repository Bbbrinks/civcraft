package nl.civcraft.core.pathfinding;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 11-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class AStarNode {
    private final GameObject gameObject;
    private AStarNode previous;
    private float hCost;
    private float gCost;

    public AStarNode(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    public AStarNode getPrevious() {
        return previous;
    }

    public void setPrevious(AStarNode previous) {
        this.previous = previous;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public float gethCost() {
        return hCost;
    }

    public void sethCost(float hCost) {
        this.hCost = hCost;
    }

    public void setgCost(float gCost) {
        this.gCost = gCost;
    }


    public float getgCost() {
        return gCost;
    }


    public float calcGCost(AStarNode current) {
        float g = 1.0f;
        if (current.getGameObject().getTransform().getTranslation().getY() < getGameObject().getTransform().getTranslation().getY()) {
            g = 1.5f;
        }

        return current.getgCost() + hCost + g;
    }

    public void setgCost(AStarNode current) {


        this.gCost = calcGCost(current);
    }
}
