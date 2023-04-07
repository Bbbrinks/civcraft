package nl.civcraft.opengl;


import com.google.inject.CreationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.spi.Message;
import nl.civcraft.core.worldgeneration.WorldGenerator;
import nl.civcraft.opengl.engine.GameEngine;
import nl.civcraft.opengl.modules.CivCraftOpenGlModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CivcraftOpenGl {
    private static final Logger LOGGER = LogManager.getLogger();

    public void run() {
        Injector injector = Guice.createInjector(new CivCraftOpenGlModule());
        try {
            boolean vSync = true;
            GameEngine gameEngine = injector.getInstance(GameEngine.class);
            WorldGenerator worldGenerator = injector.getInstance(WorldGenerator.class);
            gameEngine.start();
        } catch (CreationException ce) {
            for (Message message : ce.getErrorMessages()) {
                LOGGER.error(message);
            }
        } catch (Exception excp) {
            LOGGER.error(excp);
            System.exit(-1);
        }

    }


    public static void main(String[] args) {
        new CivcraftOpenGl().run();
    }
}
