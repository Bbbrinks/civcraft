package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.PrefabManager;

import java.util.Map;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class DropOnDestoyed extends AbstractGameComponent {

    private final Map<PrefabManager, Integer> drops;

    public DropOnDestoyed(Map<PrefabManager, Integer> drops) {
        this.drops = drops;
    }


    @Override
    public void destroyed() {
        drops.forEach((prefabManager, count) -> {
            for (int i = 0; i < count; i++) {
                prefabManager.build(getGameObject().getTransform().clone(), true);
            }
        });
    }

    public static class Factory implements GameComponentFactory<DropOnDestoyed> {

        private final Map<PrefabManager, Integer> drops;

        public Factory(Map<PrefabManager, Integer> drops) {
            this.drops = drops;
        }

        @Override
        public DropOnDestoyed build() {
            return new DropOnDestoyed(drops);
        }

        @Override
        public Class<DropOnDestoyed> getComponentType() {
            return DropOnDestoyed.class;
        }
    }

}
