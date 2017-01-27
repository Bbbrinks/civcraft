package nl.civcraft.core.gamecomponents;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class ItemComponent extends AbstractGameComponent {
    private final String type;
    private boolean inInventory = true;

    public ItemComponent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public boolean isInInventory() {
        return inInventory;
    }

    public void setInInventory(boolean inInventory) {
        if (this.inInventory != inInventory) {
            this.inInventory = inInventory;
            gameObject.changed();
        }
    }

    public static class Factory implements GameComponentFactory<ItemComponent> {
        private final String type;

        @SuppressWarnings("SameParameterValue")
        public Factory(String type) {
            this.type = type;
        }

        @Override
        public ItemComponent build() {
            return new ItemComponent(type);
        }

        @Override
        public Class<ItemComponent> getComponentType() {
            return ItemComponent.class;
        }
    }
}
