package nl.civcraft.core.rendering;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.event.SystemUpdate;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.Voxel;
import nl.civcraft.core.model.events.StockpileCreated;
import org.springframework.beans.factory.annotation.Autowired;
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
public class StockpileRenderer {

    private final Node stockpileNode;
    private final List<Stockpile> stockpiles;
    private final Spatial stockpileSpatial;

    @Autowired
    public StockpileRenderer(Node rootNode, Spatial stockpileSpatial) {
        stockpileNode = new Node("stockpiles");
        rootNode.attachChild(stockpileNode);
        stockpiles = new CopyOnWriteArrayList<>();
        this.stockpileSpatial = stockpileSpatial;
    }

    @EventListener
    public void update(SystemUpdate systemUpdate) {
        stockpileNode.detachAllChildren();
        for (Stockpile stockpile : stockpiles) {
            for (Voxel voxel : stockpile.getVoxels()) {
                Spatial clone = stockpileSpatial.clone();
                clone.setLocalTranslation(clone.getLocalTranslation().add(voxel.getX(), voxel.getY(), voxel.getZ()));
                stockpileNode.attachChild(clone);
            }
        }
    }

    @EventListener
    public void addCivvy(StockpileCreated stockpileCreated) {
        stockpiles.add(stockpileCreated.getStockpile());
    }


}
