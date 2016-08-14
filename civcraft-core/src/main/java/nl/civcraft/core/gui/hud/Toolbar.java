package nl.civcraft.core.gui.hud;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import nl.civcraft.core.input.VoxelSelectionInput;
import nl.civcraft.core.interaction.tools.StockpileTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Toolbar {

    private final Container hud;

    @Autowired
    public Toolbar(Container hud, StockpileTool stockpileTool, VoxelSelectionInput voxelSelectionInput) {
        this.hud = hud;

        Button stockpile = hud.addChild(new Button("Stockpile"));
        stockpile.addClickCommands((Command<Button>) button -> voxelSelectionInput.setCurrentTool(stockpileTool));
    }
}
