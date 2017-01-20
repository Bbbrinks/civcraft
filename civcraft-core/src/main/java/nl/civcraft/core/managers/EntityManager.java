package nl.civcraft.core.managers;

import com.jme3.math.Transform;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.GameObject;
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
    private final PrefabManager prefabManager;

    @Autowired
    public EntityManager(ApplicationEventPublisher publisher, @Qualifier("item") PrefabManager prefabManager) {
        this.publisher = publisher;
        entities = new ArrayList<>();
        this.prefabManager = prefabManager;
    }

    /***
     * Spawn item entity on the voxel
     * @param groundAt
     * @param item
     */
    public void addEntity(GameObject item, Transform transform) {
        GameObject build = prefabManager.build(transform, true);
        build.getComponent(ItemComponent.class).get().setItem(item);
        publisher.publishEvent(new EntityCreatedEvent(build, this));
    }
}
