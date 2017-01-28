package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.GameObject;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public class HarvestFromInventory extends AbstractGameComponent implements Harvestable, GameComponent {


    @Override
    public Optional<GameObject> harvest(GameObject harvester) {
        Optional<InventoryComponent> component = gameObject.getComponent(InventoryComponent.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("HarvestFromInventory can only be added to GameObjects with an Inventory component");
        }
        return component.get().getFirstItem();

    }

    @Override
    public void addTo(GameObject gameObject) {
        Optional<InventoryComponent> component = gameObject.getComponent(InventoryComponent.class);
        if (!component.isPresent()) {
            throw new IllegalStateException("HarvestFromInventory can only be added to GameObjects with an Inventory component");
        }
        super.addTo(gameObject);
    }


    public static class Factory implements GameComponentFactory<HarvestFromInventory> {
        @Override
        public HarvestFromInventory build() {
            return new HarvestFromInventory();
        }

        @Override
        public Class<HarvestFromInventory> getComponentType() {
            return HarvestFromInventory.class;
        }
    }
}
