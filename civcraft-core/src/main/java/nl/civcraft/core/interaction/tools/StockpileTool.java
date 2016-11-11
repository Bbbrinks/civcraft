package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Stockpile;
import nl.civcraft.core.model.Voxel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class StockpileTool extends GroundRectangleSelector {

    private Stockpile createdStockpile;

    @Autowired
    public StockpileTool(CurrentVoxelHighlighter currentVoxelHighlighter, ApplicationEventPublisher eventPublisher, WorldManager worldManager) {
        super(currentVoxelHighlighter, eventPublisher, worldManager);
    }

    @Override
    protected void startSelection() {
        createdStockpile = new Stockpile();
    }

    @Override
    protected void handleSelection(Voxel voxel) {
        createdStockpile.addVoxel(voxel);
    }

    @Override
    protected void endSelection() {
        worldManager.getWorld().addStockpile(createdStockpile);
        createdStockpile = null;
    }

}
