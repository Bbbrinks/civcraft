package nl.civcraft.jme3.nl.civcraft.jme3.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;
import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.interaction.util.CurrentVoxelHighlighter;
import nl.civcraft.core.modules.CivCraftCoreModule;
import nl.civcraft.core.rendering.ItemRenderer;
import nl.civcraft.core.rendering.VoxelRenderer;
import nl.civcraft.jme3.nl.civcraft.jme3.Civcraft;
import nl.civcraft.jme3.nl.civcraft.jme3.debug.DebugStatsState;
import nl.civcraft.jme3.nl.civcraft.jme3.gamecomponents.ItemRendererImpl;
import nl.civcraft.jme3.nl.civcraft.jme3.gamecomponents.StateBasedVoxelRendererImpl;
import nl.civcraft.jme3.nl.civcraft.jme3.gamecomponents.StaticVoxelRenderer;
import nl.civcraft.jme3.nl.civcraft.jme3.gui.hud.Toolbar;
import nl.civcraft.jme3.nl.civcraft.jme3.interaction.util.CurrentVoxelHighlighterJme3;
import nl.civcraft.jme3.nl.civcraft.jme3.ChunkOptimizer;
import nl.civcraft.jme3.nl.civcraft.jme3.CurrentTaskRenderer;
import nl.civcraft.jme3.nl.civcraft.jme3.SunLighting;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class CivCraftJme3Module extends AbstractModule {
    @Override
    protected void configure() {

        install(new CivCraftCoreModule());
        install(new ControlsModule());
        install(new SpatialsModule());

        bind(Civcraft.class).toInstance(Civcraft.getInstance());

        bind(InputManager.class).toInstance(Civcraft.getInstance().getInputManager());
        bind(MouseInput.class).toInstance(Civcraft.getInstance().getContext().getMouseInput());
        bind(KeyInput.class).toInstance(Civcraft.getInstance().getContext().getKeyInput());

        bind(AssetManager.class).toInstance(Civcraft.getInstance().getAssetManager());

        bind(Spatial.class).annotatedWith(Names.named("rootNode")).toInstance(Civcraft.getInstance().getRootNode());
        bind(Node.class).annotatedWith(Names.named("rootNode")).toInstance(Civcraft.getInstance().getRootNode());

        bind(ViewPort.class).annotatedWith(Names.named("mainViewPort")).toInstance(Civcraft.getInstance().getViewPort());
        bind(ViewPort.class).annotatedWith(Names.named("guiViewPort")).toInstance(Civcraft.getInstance().getGuiViewPort());

        bind(new TypeLiteral<GameComponent.GameComponentFactory<ItemRenderer>>() {
        }).to(ItemRendererImpl.ItemRendererFactory.class).in(Singleton.class);


        bind(VoxelRenderer.StateBasedVoxelRendererFactoryFactory.class).to(StateBasedVoxelRendererImpl.StateBasedVoxelRendererFactoryImpl.class);
        bind(VoxelRenderer.StaticVoxelRendererFactory.class).to(StaticVoxelRenderer.StaticVoxelRenderFactoryImpl.class);

        bind(CurrentVoxelHighlighter.class).to(CurrentVoxelHighlighterJme3.class).in(Singleton.class);

        bind(CurrentTaskRenderer.class).asEagerSingleton();
        bind(ChunkOptimizer.class).asEagerSingleton();
        bind(DebugStatsState.class).asEagerSingleton();
        bind(SunLighting.class).asEagerSingleton();

        bind(Toolbar.class).asEagerSingleton();
    }

    @Provides
    @Named("guiFont")
    @Singleton
    public BitmapFont guiFont(AssetManager assetManager) {
        return assetManager.loadFont("Interface/Fonts/Default.fnt");
    }

    @Provides
    @Named("fpsText")
    @Singleton
    public BitmapText fpsText(@Named("guiFont") BitmapFont guiFont) {
        BitmapText fpsText = new BitmapText(guiFont, false);
        fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
        fpsText.setText("Frames per second");
        fpsText.setCullHint(Spatial.CullHint.Never);
        return fpsText;
    }

    @Provides
    @Named("logMessageText")
    @Singleton
    public BitmapText logMessageText(@Named("guiFont") BitmapFont guiFont) {
        BitmapText logMessageText = new BitmapText(guiFont, false);
        logMessageText.setLocalTranslation(0, logMessageText.getLineHeight() * 2, 0);
        logMessageText.setText("");
        logMessageText.setCullHint(Spatial.CullHint.Never);
        return logMessageText;
    }

    @Provides
    @Named("hudContainer")
    @Singleton
    public Container hudContainer(Civcraft civcraft) {
        GuiGlobals.initialize(civcraft);

        // Load the 'glass' style
        BaseStyles.loadGlassStyle();

        // Set 'glass' as the default style when not specified
        GuiGlobals.getInstance().getStyles().setDefaultStyle("glass");

        // Create a simple container for our elements
        Container myWindow = new Container();
        civcraft.getGuiNode().attachChild(myWindow);

        myWindow.setLocalTranslation(0, civcraft.getSettings().getHeight(), 0);
        return myWindow;
    }

    @Provides
    @Named("guiNode")
    @Singleton
    public Node guiNode(Civcraft civcraft) {
        return civcraft.getGuiNode();
    }


    @Provides
    @Singleton
    public Camera camera(Civcraft civcraft) {
        Camera cam = civcraft.getCamera();
        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 1f, 1000f);
        cam.setLocation(new Vector3f(0f, 0f, 10f));
        cam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
        return cam;
    }

    @Provides
    @Named("chunks")
    @Singleton
    public Node chunks(@Named("rootNode") Node rootNode) {
        rootNode.detachChildNamed("chunks");
        Node chunks = new Node("chunks");
        rootNode.attachChild(chunks);
        return chunks;
    }


}
