package nl.civcraft.core.gamecomponents;

import com.jme3.math.Transform;
import nl.civcraft.core.managers.PrefabManager;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Placable extends AbstractGameComponent {
    private final PrefabManager prefabManager;
    private final boolean destroyAfterPlacing;

    public Placable(PrefabManager prefabManager,
                    boolean destroyAfterPlacing) {
        this.prefabManager = prefabManager;
        this.destroyAfterPlacing = destroyAfterPlacing;
    }

    public void place(Transform locationToPlace) {
        this.prefabManager.build(locationToPlace, true);
        if (destroyAfterPlacing) {
            gameObject.destroy();
        }
    }

    public static class Factory implements GameComponentFactory<Placable> {

        private final PrefabManager prefabManager;
        private final boolean destroyAfterPlacing;

        public Factory(PrefabManager prefabManager,
                       boolean destroyAfterPlacing) {
            this.prefabManager = prefabManager;
            this.destroyAfterPlacing = destroyAfterPlacing;
        }

        @Override
        public Placable build() {
            return new Placable(prefabManager, destroyAfterPlacing);
        }

        @Override
        public Class<Placable> getComponentType() {
            return Placable.class;
        }
    }
}
