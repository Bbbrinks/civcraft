package nl.civcraft.core.interaction.tools;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.Voxel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class StockpileTool extends GroundRectangleSelector {

    private Stockpile createdStockpile;

    @Autowired
    public StockpileTool(CurrentVoxelHighlighter currentVoxelHighlighter, Node selectionBoxes, Spatial hoverSpatial, WorldManager worldManager) {
        super(currentVoxelHighlighter, selectionBoxes, hoverSpatial, worldManager);
    }

    @Override
    protected void startSelection() {
        createdStockpile = new Stockpile();
    }

    @Override
    protected void handleSelection(int x, int y, int z) {
        Optional<Voxel> voxelAt = worldManager.getWorld().getVoxelAt(x, y, z);
        if (voxelAt.isPresent()) {
            createdStockpile.addVoxel(voxelAt.get());
        }
    }

    @Override
    protected void endSelection() {
        worldManager.getWorld().addStockpile(createdStockpile);
        createdStockpile = null;
    }

}
