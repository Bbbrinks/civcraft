package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.PrefabManager;

/**
 * Created by Bob on 21-1-2017.
 * <p>
 * This is probably not worth documenting
 */
public class ManagedObject extends AbstractGameComponent implements GameComponent {

    private final PrefabManager prefabManager;

    public ManagedObject(PrefabManager prefabManager) {
        this.prefabManager = prefabManager;
    }

    @Override
    public void destroyed() {
        prefabManager.destroy(gameObject);
        super.destroyed();
    }
}
