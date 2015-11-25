package nl.civcraft.core.worldgeneration;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

/**
 * Created by Bob on 25-11-2015.
 * <p>
 * This is probably not worth documenting
 */
public class Voxel extends Node {

    public Voxel(int x, int y, int z, AssetManager assetManager) {
        Box box1 = new Box(0.5f,0.5f,0.5f);
        Geometry blue = new Geometry("Box", box1);
        setLocalTranslation(new Vector3f(x,y,z));
        Material mat1 = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");

        mat1.setColor("Color", ColorRGBA.randomColor());
        blue.setMaterial(mat1);
        this.attachChild(blue);
    }
}
