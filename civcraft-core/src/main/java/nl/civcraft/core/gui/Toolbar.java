package nl.civcraft.core.gui;

import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Toolbar extends Node {


    private final Nifty nifty;

    @Autowired
    public Toolbar(NiftyJmeDisplay niftyDisplay) {
        nifty = niftyDisplay.getNifty();
        nifty.addScreen("hud", new ScreenBuilder("hud") {{
            controller(new DefaultScreenController());
            layer(new LayerBuilder("toolbarLayer") {{
                childLayoutVertical();
                panel(new PanelBuilder("toolbar") {{
                    childLayoutCenter();

                    control(new ButtonBuilder("harvest", "Harvest") {{
                        childLayoutCenter();
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                    }});
                }});
            }});
        }}.build(nifty));
        nifty.gotoScreen("hud");
    }


}
