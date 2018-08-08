package nl.civcraft.opengl.hud;

import com.google.inject.Singleton;
import nl.civcraft.core.interaction.MouseTool;
import nl.civcraft.core.interaction.MouseToolManager;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.hud.container.PercentagePanel;
import nl.civcraft.opengl.hud.container.VerticalFlowPanel;
import nl.civcraft.opengl.hud.control.Button;
import nl.civcraft.opengl.hud.font.Font;
import nl.civcraft.opengl.hud.font.FontFamilyManager;
import nl.civcraft.opengl.interaction.CameraMovement;
import nl.civcraft.opengl.interaction.MouseInputManager;
import nl.civcraft.opengl.interaction.ToolManager;
import org.joml.Vector4f;

import javax.inject.Inject;
import java.io.IOException;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;

@Singleton
public class Hud {
    private HudElement rootElement;
    private final NanoVGContext nanoVgContent;
    private final FontFamilyManager fontFamilyManager;
    private final MouseToolManager mouseToolManager;

    @Inject
    public Hud(NanoVGContext nanoVgContent,
               FontFamilyManager fontFamilyManager,
               MouseInputManager mouseInputManager,
               MouseToolManager mouseToolManager,
               CameraMovement cameraMovement,
               ToolManager toolManager) {
        this.nanoVgContent = nanoVgContent;
        this.mouseToolManager = mouseToolManager;
        this.fontFamilyManager = fontFamilyManager;

        mouseInputManager.registerMovementListener(mouseMovement -> {
            if (!rootElement.handleMouseMovement(mouseInputManager.getMousePosition())) {
                cameraMovement.handleMouseMovement(mouseMovement);
                toolManager.handleMovement(mouseMovement);
            }
        });
        mouseInputManager.registerLeftClickListener(() -> {
            if (rootElement.isHover()) {
                rootElement.handleLeftClick();
            } else {
                toolManager.handleMouseClick();
            }
        });
    }


    public void render(Window window) {
        nvgBeginFrame(nanoVgContent.getVg(), window.getWidth(), window.getHeight(), 1);
        rootElement.render(nanoVgContent, 0, 0, window.getWidth(), window.getHeight());
        nvgEndFrame(nanoVgContent.getVg());

        // Restore state
        window.restoreState();
    }

    public void cleanup() {
        rootElement.cleanup();
    }

    public void init() throws IOException {
        nanoVgContent.init();
        Font font = new Font(new Vector4f(0, 0, 0, 1f), fontFamilyManager.loadFont("open-sans", "/fonts/OpenSans-Bold.ttf"), 10f, nanoVgContent);
        PercentagePanel percentagePanel = new PercentagePanel(0.05f, 1);
        VerticalFlowPanel verticalFlowPanel = new VerticalFlowPanel();
        percentagePanel.addChild(verticalFlowPanel);
        for (MouseTool mouseTool : mouseToolManager.getMouseTools()) {
            Button button = new Button(mouseTool.getLabel(),
                    font,
                    new Vector4f(1, 1, 1, 1),
                    new Vector4f(0.7f, 0.7f, 0.7f, 1),
                    () -> mouseToolManager.setSelectedMouseTool(mouseTool));
            verticalFlowPanel.addChild(button);
        }
        rootElement = percentagePanel;
    }
}
