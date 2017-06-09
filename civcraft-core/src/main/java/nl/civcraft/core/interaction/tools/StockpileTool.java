package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Bob on 14-8-2016.
 * <p>
 * This is probably not worth documenting
 */
public class StockpileTool extends GroundRectangleSelector {


    private final PrefabManager prefabManager;
    private Stockpile createdStockpile;

    @Inject
    public StockpileTool(CurrentVoxelHighlighter currentVoxelHighlighter,
                         VoxelManager voxelManager,
                         @Named("stockpile") PrefabManager prefabManager,
                         @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        super(currentVoxelHighlighter, voxelManager, voxelHighlightManager);
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
