package nl.civcraft.opengl.rendering.geometry;

import nl.civcraft.opengl.rendering.Geometry;
import nl.civcraft.opengl.rendering.material.Material;

import java.util.ArrayList;

/**
 * Created by Bob on 12-1-2018.
 * <p>
 * This is probably not worth documenting
 */
public class Box extends Geometry {

    public Box(Material material) {
        super(new ArrayList<>(), material);
        meshes.add(Quad.back());
        meshes.add(Quad.bottom());
        meshes.add(Quad.front());
        meshes.add(Quad.left());
        meshes.add(Quad.right());
        meshes.add(Quad.top());
        updateBounds();
    }


}
