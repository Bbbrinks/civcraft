package nl.civcraft.core.managers;

import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.gamecomponents.ItemRenderer;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import nl.civcraft.core.model.Voxel;
import org.springframework.beans.factory.annotation.Autowired;
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


    private final ItemRenderer itemRenderer;
    private final List<GameObject> entities;

    @Autowired
    public EntityManager(ItemRenderer itemRenderer) {
        this.itemRenderer = itemRenderer;
        entities = new ArrayList<>();
    }

    /***
     * Spawn item entity on the voxel
     * @param item
     * @param groundAt
     */
    public void addEntity(Item item, Voxel groundAt) {
        GameObject gameObject = new GameObject();
        gameObject.addComponent(new ItemComponent(item));
        gameObject.addComponent(new Physical(groundAt.getLocation().add(0, 0.2f, 0)));
        gameObject.addComponent(itemRenderer);
        entities.add(gameObject);
    }
}
