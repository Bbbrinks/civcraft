package nl.civcraft.core;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civcraft extends SimpleApplication {

    private static Civcraft instance;

    private Civcraft() {
        super(new CivCraftAppState());
        this.showSettings = false;
    }

    /**
     * Starts civcraft
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        instance = new Civcraft();
        instance.start();
    }

    public static Civcraft getInstance() {
        return instance;
    }

    @Override
    public void simpleInitApp() {
        //NoOp
    }

    public boolean isPaused() {
        return paused;
    }

    public AppSettings getSettings() {
        return settings;

    }
}
