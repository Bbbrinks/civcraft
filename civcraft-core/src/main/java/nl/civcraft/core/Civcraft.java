package nl.civcraft.core;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Civcraft extends SimpleApplication {

    public static Civcraft instance;

    public Civcraft() {
        super(new CivCraftAppState());
        this.showSettings = false;
    }

    public static void main(String[] args) {
        instance = new Civcraft();
        instance.start();
    }

    @Override
    public void simpleInitApp() {

    }

    public AppSettings getSettings() {
        return settings;

    }
}
