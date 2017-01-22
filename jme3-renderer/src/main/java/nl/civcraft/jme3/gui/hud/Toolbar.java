package nl.civcraft.jme3.gui.hud;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.jme3.input.VoxelSelectionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class Toolbar {

    @Autowired
    public Toolbar(Container hud, List<MouseTool> tools, VoxelSelectionInput voxelSelectionInput) {

        for (MouseTool tool : tools) {
            Button button = hud.addChild(new Button(tool.getLabel()));
            //noinspection unchecked
            button.addClickCommands((Command<Button>) button1 -> voxelSelectionInput.setCurrentTool(tool));
        }
    }
}
