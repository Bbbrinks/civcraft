package nl.civcraft.core.conf;

import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import nl.civcraft.core.Civcraft;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GuiConfiguration {

    @Bean
    public Container hudContainer(Civcraft civcraft) {
        GuiGlobals.initialize(civcraft);

        // Load the 'glass' style
        BaseStyles.loadGlassStyle();

        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        // Create a simple container for our elements
        Container myWindow = new Container();
        civcraft.getGuiNode().attachChild(myWindow);
        AppSettings settings = civcraft.getSettings();

        myWindow.setLocalTranslation(0, 20, 0);
        return myWindow;
    }

    @Bean
    public Node guiNode(Civcraft civcraft) {
        return civcraft.getGuiNode();
    }

    @Bean
    public Node selectionBoxes(Civcraft civcraft) {
        Node selectionBoxes = new Node("selectionBoxes");

        civcraft.getRootNode().attachChild(selectionBoxes);
        return selectionBoxes;
    }

}
