package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
@Component
public class StockpileTool extends GroundRectangleSelector {


    private final PrefabManager prefabManager;
    private Stockpile createdStockpile;

    @Autowired
    public StockpileTool(CurrentVoxelHighlighter currentVoxelHighlighter, ApplicationEventPublisher eventPublisher, VoxelManager voxelManager, @Qualifier("stockpile") PrefabManager prefabManager) {
        super(currentVoxelHighlighter, eventPublisher, voxelManager);
        this.prefabManager = prefabManager;
    }

    @Override
    protected void startSelection() {
        GameObject newStockpile = prefabManager.build(startingVoxel.getTransform().clone(), true);
        createdStockpile = newStockpile.getComponent(Stockpile.class).orElseThrow(() -> new IllegalStateException("Stockpiles should be stockpiles"));
    }

    @Override
    protected void endSelection() {
        createdStockpile = null;
    }

    @Override
    protected void handleSelection(GameObject voxel) {
        createdStockpile.addVoxel(voxel);
    }

    @Override
    public String getLabel() {
        return "Stockpile";
    }

}
