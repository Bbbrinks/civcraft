package nl.civcraft.jme3.gui.hud;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import nl.civcraft.core.interaction.tools.BreakBlokTool;
import nl.civcraft.core.interaction.tools.HarvestTool;
import nl.civcraft.core.interaction.tools.LevelGroundTool;
import nl.civcraft.core.interaction.tools.StockpileTool;
import nl.civcraft.jme3.input.VoxelSelectionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Toolbar {

    private final Container hud;

    @Autowired
    public Toolbar(Container hud, StockpileTool stockpileTool, VoxelSelectionInput voxelSelectionInput, HarvestTool harvestTool, LevelGroundTool levelGroundTool, BreakBlokTool breakBlokTool) {
        this.hud = hud;

        Button stockpile = hud.addChild(new Button("Stockpile"));
        stockpile.addClickCommands((Command<Button>) button -> voxelSelectionInput.setCurrentTool(stockpileTool));

        Button harvest = hud.addChild(new Button("Harvest"));
        harvest.addClickCommands((Command<Button>) button -> voxelSelectionInput.setCurrentTool(harvestTool));

        Button level = hud.addChild(new Button("Level ground"));
        level.addClickCommands((Command<Button>) button -> voxelSelectionInput.setCurrentTool(levelGroundTool));

        Button breakBlock = hud.addChild(new Button("Break block"));
        breakBlock.addClickCommands((Command<Button>) button -> voxelSelectionInput.setCurrentTool(breakBlokTool));
    }
}
