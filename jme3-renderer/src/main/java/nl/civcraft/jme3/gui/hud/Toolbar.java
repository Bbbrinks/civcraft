package nl.civcraft.jme3.gui.hud;

import com.simsilica.lemur.Button;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.Container;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.jme3.input.VoxelSelectionInput;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Set;


public class Toolbar {

    @Inject
    public Toolbar(@Named("hudContainer") Container hud,
                   Set<MouseTool> tools,
                   VoxelSelectionInput voxelSelectionInput) {

        for (MouseTool tool : tools) {
            Button button = hud.addChild(new Button(tool.getLabel()));
            //noinspection unchecked
            button.addClickCommands((Command<Button>) button1 -> voxelSelectionInput.setCurrentTool(tool));
        }
    }
}
