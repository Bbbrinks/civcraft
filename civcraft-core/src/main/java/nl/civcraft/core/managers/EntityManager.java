package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import nl.civcraft.core.model.events.EntityCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class EntityManager {


    private final List<GameObject> entities;
    private final ApplicationEventPublisher publisher;
    private final GameObjectManager gameObjectManager;

    @Autowired
    public EntityManager(ApplicationEventPublisher publisher, @Qualifier("item") GameObjectManager gameObjectManager) {
        this.publisher = publisher;
        entities = new ArrayList<>();
        this.gameObjectManager = gameObjectManager;
    }

    /***
     * Spawn item entity on the voxel
     * @param item
     * @param groundAt
     */
    public void addEntity(Item item, Transform transform) {
        GameObject build = gameObjectManager.build(transform);
        build.getComponent(ItemComponent.class).get().setItem(item);
        publisher.publishEvent(new EntityCreatedEvent(build, this));
    }
}
