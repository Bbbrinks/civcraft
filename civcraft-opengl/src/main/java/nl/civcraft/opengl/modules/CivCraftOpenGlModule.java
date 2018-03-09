package nl.civcraft.opengl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import nl.civcraft.core.interaction.MouseInputManagerInterface;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.modules.CivCraftCoreModule;
import nl.civcraft.opengl.interaction.CameraMovement;
import nl.civcraft.opengl.interaction.MouseInputManager;
import nl.civcraft.opengl.interaction.ToolManager;
import nl.civcraft.opengl.raycast.MousePicker;
import nl.civcraft.opengl.rendering.CivvyRenderer;
import nl.civcraft.opengl.rendering.DebugRenderer;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.rendering.VoxelRenderer;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftOpenGlModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new CivCraftCoreModule());
        bind(VoxelRenderer.class).asEagerSingleton();
        bind(CivvyRenderer.class).asEagerSingleton();
        bind(CameraMovement.class).asEagerSingleton();
        bind(MousePicker.class).asEagerSingleton();
        bind(DebugRenderer.class).asEagerSingleton();
        bind(ToolManager.class).asEagerSingleton();
        bind(CurrentVoxelHighlighter.class).to(nl.civcraft.opengl.interaction.CurrentVoxelHighlighter.class);
        bind(MouseInputManagerInterface.class).to(MouseInputManager.class);
        bind(Key.get(Node.class, Names.named("rootNode"))).toInstance(new Node("root",null));
        bind(Key.get(Node.class, Names.named("debugNode"))).toInstance(new Node("debugNode",null));
    }


}