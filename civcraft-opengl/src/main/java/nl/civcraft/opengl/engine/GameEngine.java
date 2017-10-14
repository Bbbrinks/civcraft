package nl.civcraft.opengl.engine;

import com.google.inject.Inject;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import nl.civcraft.opengl.interaction.KeyboardInputManager;
import nl.civcraft.opengl.interaction.MouseInputManager;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.rendering.Renderer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Named;

public class GameEngine implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Thread gameLoopThread;

    private final Renderer renderer;
    private final Node rootNode;

    private final WorldGenerator worldGenerator;
    private final KeyboardInputManager keyboardInputManager;
    private final MouseInputManager mouseInputManager;
    private final Timer timer;


    @Inject
    public GameEngine(Window window,
                      Renderer renderer,
                      @Named("rootNode") Node rootNode,
                      WorldGenerator worldGenerator,
                      KeyboardInputManager keyboardInputManager,
                      MouseInputManager mouseInputManager,
                      Timer timer) throws Exception {
        this.window = window;
        this.renderer = renderer;
        this.worldGenerator = worldGenerator;
        this.keyboardInputManager = keyboardInputManager;
        this.mouseInputManager = mouseInputManager;
        this.timer = timer;
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        this.rootNode = rootNode;
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
        timer.init();
        mouseInputManager.init(window);
    }

    protected void gameLoop() {

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            float elapsedTime = timer.getElapsedTime();
            render();
            keyboardInputManager.update(window, elapsedTime);
            mouseInputManager.input(window);
        }
    }

    protected void cleanup() {

        window.cleanup();
    }


    protected void render() {
        renderer.render(window, rootNode);
        window.update();
    }
}
