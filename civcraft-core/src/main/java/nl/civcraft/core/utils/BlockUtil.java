package nl.civcraft.core.utils;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Face;

/**
 * Created by Bob on 18-9-2016.
 * <p>
 * This is probably not worth documenting
 */
public class BlockUtil {
    private static final float BLOCK_SIZE = 0.5f;

    public static Block getQuadBlock(String name, Material topMaterial, Material sideMaterial, Material bottomMaterial) {
        Block block = new Block(name);

        Geometry topGeometry = getBlockQuadGeometry(Face.TOP);
        topGeometry.setMaterial(topMaterial);
        block.attachChild(topGeometry);

        Geometry bottomGeometry = getBlockQuadGeometry(Face.BOTTOM);
        bottomGeometry.setMaterial(bottomMaterial);
        block.attachChild(bottomGeometry);

        Geometry leftGeometry = getBlockQuadGeometry(Face.LEFT);
        leftGeometry.setMaterial(sideMaterial);
        block.attachChild(leftGeometry);

        Geometry rightGeometry = getBlockQuadGeometry(Face.RIGHT);
        rightGeometry.setMaterial(sideMaterial);
        block.attachChild(rightGeometry);

        Geometry frontGeometry = getBlockQuadGeometry(Face.FRONT);
        frontGeometry.setMaterial(sideMaterial);
        block.attachChild(frontGeometry);

        Geometry backGeometry = getBlockQuadGeometry(Face.BACK);
        backGeometry.setMaterial(sideMaterial);
        block.attachChild(backGeometry);

        return block;
    }

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
}
