package nl.civcraft.core.interaction.tools;

import nl.civcraft.core.gamecomponents.Stockpile;
import nl.civcraft.core.interaction.MousePicker;
import nl.civcraft.core.interaction.selectors.GroundRectangleSelector;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.VoxelManager;
import nl.civcraft.core.model.GameObject;
import org.joml.Matrix4f;

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
    public StockpileTool(MousePicker mousePicker,
            VoxelManager voxelManager,
                         @Named("stockpile") PrefabManager prefabManager,
                         @Named("voxelHighlight") PrefabManager voxelHighlightManager) {
        super(mousePicker, voxelManager, voxelHighlightManager);
        this.prefabManager = prefabManager;
    }

    @Override
    protected void startSelection() {
        GameObject newStockpile = prefabManager.build(new Matrix4f(startingVoxel.getTransform()), true);
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
