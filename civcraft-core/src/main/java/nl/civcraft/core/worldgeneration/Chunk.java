package nl.civcraft.core.worldgeneration;

import com.jme3.scene.Node;
import com.jme3.scene.control.LodControl;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Chunk extends Node {

    public Chunk(LodControl lodControl) {
        addControl(lodControl);
    }
}
