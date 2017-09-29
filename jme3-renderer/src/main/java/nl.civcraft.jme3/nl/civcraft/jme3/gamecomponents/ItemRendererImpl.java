package nl.civcraft.jme3.nl.civcraft.jme3.gamecomponents;

import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.rendering.ItemRenderer;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */


public class ItemRendererImpl extends AbstractGameComponent implements ItemRenderer {


    @Override
    public boolean isVisible() {
        return gameObject.getComponent(ItemComponent.class).map(i -> !i.isInInventory()).orElse(false);
    }

    public static class ItemRendererFactory implements GameComponentFactory<ItemRenderer> {

        @Override
        public ItemRendererImpl build() {
            return new ItemRendererImpl();
        }

        @Override
        public Class<ItemRenderer> getComponentType() {
            return ItemRenderer.class;
        }
    }
}
