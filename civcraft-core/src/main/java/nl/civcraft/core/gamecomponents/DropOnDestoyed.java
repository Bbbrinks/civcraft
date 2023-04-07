package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.PrefabManager;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by Bob on 21-4-2017.
 * <p>
 * This is probably not worth documenting
 */
public class DropOnDestoyed extends AbstractGameComponent {

    private final Map<Supplier<PrefabManager>, Integer> drops;

    public DropOnDestoyed(Map<Supplier<PrefabManager>, Integer> drops) {
        this.drops = drops;
    }


    @Override
    public void destroyed() {
        drops.forEach((prefabManager, count) -> {
            for (int i = 0; i < count; i++) {
                prefabManager.get().build(new Matrix4f(getGameObject().getTransform()), true);
            }
        });
    }

    public static class Factory implements GameComponentFactory<DropOnDestoyed> {


        private final Map<Supplier<PrefabManager>, Integer> drops;

        public Factory(Map<Supplier<PrefabManager>, Integer> drops) {
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

        public Map<Supplier<PrefabManager>, Integer> getDrops() {
            return drops;
        }
    }

}
