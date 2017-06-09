package nl.civcraft.jme3.rendering;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.SystemEventPublisher;
import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
class StockpileRenderer {

    private final Node stockpileNode;
    private final List<Stockpile> stockpiles;
    private final Spatial stockpileSpatial;

    @Autowired
    public StockpileRenderer(Node rootNode,
                             Spatial stockpileSpatial,
                             SystemEventPublisher systemEventPublisher,
                             @Qualifier("stockpile") PrefabManager stockpileManager) {
        stockpileManager.getGameObjectCreated().subscribe(this::stockpileCreated);
        systemEventPublisher.getPublisher().subscribe(this::update);
        stockpileNode = new Node("stockpiles");
        rootNode.attachChild(stockpileNode);
        stockpiles = new CopyOnWriteArrayList<>();
        this.stockpileSpatial = stockpileSpatial;
    }

    @EventListener
    public void update(float tpf) {
        stockpileNode.detachAllChildren();
        for (Stockpile stockpile : stockpiles) {
            for (GameObject voxel : stockpile.getVoxels()) {
                Spatial clone = stockpileSpatial.clone();
                clone.setLocalTranslation(clone.getLocalTranslation().add(voxel.getTransform().getTranslation()));
                stockpileNode.attachChild(clone);
            }
        }
    }

    @EventListener
    public void stockpileCreated(GameObject stockpileCreated) {
        stockpileCreated.getComponent(Stockpile.class).ifPresent(stockpiles::add);
    }


}
