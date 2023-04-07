package nl.civcraft.opengl.engine;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import nl.civcraft.opengl.interaction.KeyboardInputManager;
import nl.civcraft.opengl.interaction.MouseInputManager;
import nl.civcraft.opengl.hud.Hud;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.rendering.Renderer;
import nl.civcraft.opengl.rendering.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Named;

@Singleton
public class GameEngine implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Thread gameLoopThread;

    private final Renderer renderer;
    private final Scene scene;

    private final WorldGenerator worldGenerator;
    private final KeyboardInputManager keyboardInputManager;
    private final MouseInputManager mouseInputManager;
    private final Timer timer;

    private final Subject<Float> updateScene;
    private final Node debugNode;
    private final Hud hud;


    @Inject
    public GameEngine(Window window,
                      Renderer renderer,
                      Scene scene,
                      @Named("debugNode") Node debugNode,
                      WorldGenerator worldGenerator,
                      KeyboardInputManager keyboardInputManager,
                      MouseInputManager mouseInputManager,
                      Timer timer,
                      Hud hud) throws Exception {
        this.window = window;
        this.renderer = renderer;
        this.worldGenerator = worldGenerator;
        this.keyboardInputManager = keyboardInputManager;
        this.mouseInputManager = mouseInputManager;
        this.timer = timer;
        this.hud = hud;
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        this.scene = scene;
        this.debugNode = debugNode;
        updateScene = PublishSubject.create();
    }

    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            init();
            worldGenerator.run();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }

    protected void init() throws Exception {
        window.init();
        renderer.init(window);
        hud.init();
        timer.init();
        mouseInputManager.init(window);
    }

    protected void gameLoop() {

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            float elapsedTime = timer.getElapsedTime();
            updateScene.onNext(elapsedTime);
            render();
            keyboardInputManager.update(window, elapsedTime);
            mouseInputManager.input(window);
        }
    }

    protected void cleanup() {
        window.cleanup();
        renderer.cleanup();
        hud.cleanup();
    }


    protected void render() {
        renderer.render(window, scene, debugNode);
        hud.render(window);
        window.update();
    }

    public Subject<Float> getUpdateScene() {
        return updateScene;
    }
}
