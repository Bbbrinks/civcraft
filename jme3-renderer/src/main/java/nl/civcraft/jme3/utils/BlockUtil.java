package nl.civcraft.jme3.utils;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import nl.civcraft.core.model.Face;
import nl.civcraft.jme3.model.RenderedVoxelFace;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Bob on 18-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class BlockUtil {
    public static final float BLOCK_SIZE = 0.5f;
    private static final Geometry TOP = getBlockQuadGeometry(Face.TOP);
    private static final Geometry BOTTOM = getBlockQuadGeometry(Face.BOTTOM);
    private static final Geometry LEFT = getBlockQuadGeometry(Face.LEFT);
    private static final Geometry RIGHT = getBlockQuadGeometry(Face.RIGHT);
    private static final Geometry FRONT = getBlockQuadGeometry(Face.FRONT);
    private static final Geometry BACK = getBlockQuadGeometry(Face.BACK);

    public static Geometry getBlockQuadGeometry(Face face) {
        Quad quad = new Quad(BLOCK_SIZE * 2, BLOCK_SIZE * 2);
        String name = null;
        Vector3f translation = null;
        float[] rotation = null;
        switch (face) {
            case TOP:
                name = "top";
                translation = new Vector3f(-0.5f, 0.5f, 0.5f);
                rotation = new float[]{-90F * FastMath.DEG_TO_RAD, 0F, 0F};
                break;
            case BOTTOM:
                name = "bottom";
                translation = new Vector3f(-0.5f, -0.5f, -0.5f);
                rotation = new float[]{90F * FastMath.DEG_TO_RAD, 0F, 0F};
                break;
            case LEFT:
                name = "left";
                translation = new Vector3f(0.5f, -0.5F, -0.5f);
                rotation = new float[]{0F, 180F * FastMath.DEG_TO_RAD, 0F};
                break;
            case RIGHT:
                name = "right";
                translation = new Vector3f(-0.5f, -0.5F, 0.5f);
                rotation = new float[]{0F, 0F, 0F};
                break;
            case FRONT:
                name = "front";
                translation = new Vector3f(-0.5f, -0.5F, -0.5f);
                rotation = new float[]{0F, 270F * FastMath.DEG_TO_RAD, 0F};
                break;
            case BACK:
                name = "back";
                translation = new Vector3f(0.5f, -0.5F, 0.5f);
                rotation = new float[]{0F, 90F * FastMath.DEG_TO_RAD, 0F};
                break;
            default:
                break;
        }
        Geometry geometry = new Geometry(name, quad);
        Quaternion quaternion = new Quaternion(rotation);
        geometry.setLocalRotation(quaternion);
        geometry.setLocalTranslation(translation);

        return geometry;
    }

    public static Map<Face, RenderedVoxelFace> getQuadBlock(Material material) {
        return getQuadBlock(material, material, material);
    }

    public static Map<Face, RenderedVoxelFace> getQuadBlock(Material topMaterial, Material sideMaterial, Material bottomMaterial) {
        Map<Face, RenderedVoxelFace> block = new EnumMap<>(Face.class);

        block.put(Face.TOP, new RenderedVoxelFace(topMaterial, TOP.clone()));

        block.put(Face.BOTTOM, new RenderedVoxelFace(bottomMaterial, BOTTOM.clone()));

        block.put(Face.LEFT, new RenderedVoxelFace(sideMaterial, LEFT.clone()));

        block.put(Face.RIGHT, new RenderedVoxelFace(sideMaterial, RIGHT.clone()));

        block.put(Face.FRONT, new RenderedVoxelFace(sideMaterial, FRONT.clone()));

        block.put(Face.BACK, new RenderedVoxelFace(sideMaterial, BACK.clone()));

        return block;
    }
}