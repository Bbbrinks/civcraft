package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.utils.RandomUtil;

import java.util.Optional;

/**
 * Created by Bob on 25-11-2016.
 * <p>
 * This is probably not worth documenting
 */
public class RandomItemGenerator extends AbstractGameComponent {

    private final int maxItems;
    private final PrefabManager itemSupplier;

    public RandomItemGenerator(int maxItems, PrefabManager itemSupplier) {
        this.maxItems = maxItems;
        this.itemSupplier = itemSupplier;
    }

    @Override
    public void addTo(GameObject gameObject) {
        super.addTo(gameObject);
        Optional<Inventory> inventory = gameObject.getComponent(Inventory.class);
        if (!inventory.isPresent()) {
            throw new IllegalStateException("Random item generator can only be added to inventories");
        }
        Inventory limitedInventory = inventory.get();
        int nextInt = RandomUtil.getNextInt(maxItems);
        for (int i = 0; i < nextInt; i++) {
            limitedInventory.addItem(itemSupplier.build(gameObject.getTransform(), true));
        }
    }

    public static class Factory implements GameComponentFactory<RandomItemGenerator> {
        private final int maxItems;
        private final PrefabManager itemManager;

        public Factory(int maxItems, PrefabManager itemManager) {
            this.maxItems = maxItems;
            this.itemManager = itemManager;
        }

        @Override
        public RandomItemGenerator build() {
            return new RandomItemGenerator(maxItems, itemManager);
        }

        @Override
        public Class<RandomItemGenerator> getComponentType() {
            return RandomItemGenerator.class;
        }
    }
}
