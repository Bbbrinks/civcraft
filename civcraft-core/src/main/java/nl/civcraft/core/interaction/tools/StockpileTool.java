package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Stockpile;
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

    private final WorldManager worldManager;
    private Stockpile createdStockpile;

    @Autowired
    public StockpileTool(CurrentVoxelHighlighter currentVoxelHighlighter, ApplicationEventPublisher eventPublisher, VoxelManager voxelManager, WorldManager worldManager) {
        super(currentVoxelHighlighter, eventPublisher, voxelManager);
        this.worldManager = worldManager;
    }

    @Override
    protected void startSelection() {
        createdStockpile = new Stockpile();
    }

    @Override
    protected void endSelection() {
        worldManager.getWorld().addStockpile(createdStockpile);
        createdStockpile = null;
    }

    @Override
    protected void handleSelection(GameObject voxel) {
        createdStockpile.addVoxel(voxel);
    }

}
