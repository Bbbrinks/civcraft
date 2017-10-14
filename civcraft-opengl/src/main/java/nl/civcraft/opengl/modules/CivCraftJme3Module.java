package nl.civcraft.opengl.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.modules.CivCraftCoreModule;
import nl.civcraft.opengl.interaction.CameraMovement;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.rendering.voxel.VoxelRenderer;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftJme3Module extends AbstractModule {
    @Override
    protected void configure() {
        install(new CivCraftCoreModule());
        bind(VoxelRenderer.class).asEagerSingleton();
        bind(CameraMovement.class).asEagerSingleton();
        bind(CurrentVoxelHighlighter.class).to(nl.civcraft.opengl.interaction.CurrentVoxelHighlighter.class);
        bind(Key.get(Node.class, Names.named("rootNode"))).toInstance(new Node("root",null));
    }


}
