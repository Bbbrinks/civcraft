package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.events.GameObjectChangedEvent;
import nl.civcraft.core.model.events.GameObjectCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class ItemGravity {

    private final VoxelManager voxelManager;

    @Autowired
    public ItemGravity(VoxelManager voxelManager) {
        this.voxelManager = voxelManager;
    }

    @EventListener
    public void handleEntityCreated(GameObjectCreatedEvent gameObjectCreatedEvent) {
        Optional<ItemComponent> itemComponent = gameObjectCreatedEvent.getGameObject().getComponent(ItemComponent.class);
        if (itemComponent.isPresent()) {
            itemGravity(gameObjectCreatedEvent.getGameObject());
        }
    }

    private void itemGravity(GameObject gameObject) {
        Optional<GameObject> groundAt = voxelManager.getGroundAt(gameObject.getTransform().getTranslation(), 30);
        if (groundAt.isPresent()) {
            if (groundAt.get().getTransform().getTranslation().distance(gameObject.getTransform().getTranslation()) > 0.5f) {
                gameObject.getTransform().setTranslation(gameObject.getTransform().getTranslation().subtract(0, 0.1f, 0));
                gameObject.changed();
            }
        } else {
            gameObject.destroy();
        }
    }

    @EventListener
    public void handleEntityUpdated(GameObjectChangedEvent gameObjectChangedEvent) {
        Optional<ItemComponent> itemComponent = gameObjectChangedEvent.getGameObject().getComponent(ItemComponent.class);
        if (itemComponent.isPresent()) {
            itemGravity(gameObjectChangedEvent.getGameObject());
        }
    }
}
