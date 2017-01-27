package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

/**
 * Created by Bob on 30-10-2016.
 * <p>
 * This is probably not worth documenting
 */
public abstract class AbstractGameComponent implements GameComponent {
    protected GameObject gameObject;

    @Override
    public void addTo(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public GameObject getGameObject() {
        return gameObject;
    }

    @Override
    public void destroyed() {
        this.gameObject = null;
    }

    @Override
    public void changed() {
        //Noop
    }

    @Override
    public void removeFrom(GameObject gameObject) {
        this.gameObject = null;
    }
}
