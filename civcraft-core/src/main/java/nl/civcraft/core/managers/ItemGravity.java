package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 22-1-2017.
 * <p>
 * This is probably not worth documenting
 */
//TODO: refactor this to be a GameComponent
@Component
public class ItemGravity {

    private final VoxelManager voxelManager;

    @Autowired
    public ItemGravity(VoxelManager voxelManager,
                       @Qualifier("item") PrefabManager prefabManager) {
        this.voxelManager = voxelManager;
        prefabManager.getGameObjectCreated().subscribe(this::handleEntityCreated);
        prefabManager.getGameObjectChangedEvent().subscribe(this::handleEntityUpdated);
    }

    public void handleEntityCreated(GameObject gameObject) {
        Optional<ItemComponent> itemComponent = gameObject.getComponent(ItemComponent.class);
        if (itemComponent.isPresent()) {
            itemGravity(gameObject);
        }
    }

    private void itemGravity(GameObject gameObject) {
        if (gameObject.getComponent(ItemComponent.class).map(i -> !i.isInInventory()).orElse(false)) {
            Optional<GameObject> groundAt = voxelManager.getGroundAt(gameObject.getTransform().getTranslation(), 30);
            if (groundAt.isPresent() && groundAt.get().getTransform().getTranslation().distance(gameObject.getTransform().getTranslation()) > 0.5f) {
                gameObject.getTransform().setTranslation(gameObject.getTransform().getTranslation().subtract(0, 0.1f, 0));
                gameObject.changed();
            }
        }
    }

    @EventListener
    public void handleEntityUpdated(GameObject gameObject) {
        Optional<ItemComponent> itemComponent = gameObject.getComponent(ItemComponent.class);
        if (itemComponent.isPresent()) {
            itemGravity(gameObject);
        }
    }
}
