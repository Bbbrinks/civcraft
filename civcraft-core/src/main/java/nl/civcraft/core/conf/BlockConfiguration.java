package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
import nl.civcraft.core.managers.BlockManager;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Face;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockConfiguration {

    private static final float BLOCK_SIZE = 0.5f;
    @Autowired
    private AssetManager assetManager;

    @Bean
    public BlockManager voxelGeometryManager() {
        return new BlockManager();
    }

    @Bean
    public Block dirt(Material dirtMaterial) {
        return getQuadBlock("dirt", dirtMaterial, dirtMaterial, dirtMaterial);
    }

    private Block getQuadBlock(String name, Material grassTopMaterial, Material grassSideMaterial, Material grassBottomMaterial) {
        Block block = new Block(name);

        Geometry topGeometry = getBlockQuadGeometry(Face.TOP);
        topGeometry.setMaterial(grassTopMaterial);
        block.attachChild(topGeometry);

        Geometry bottomGeometry = getBlockQuadGeometry(Face.BOTTOM);
        bottomGeometry.setMaterial(grassBottomMaterial);
        block.attachChild(bottomGeometry);

        Geometry leftGeometry = getBlockQuadGeometry(Face.LEFT);
        leftGeometry.setMaterial(grassSideMaterial);
        block.attachChild(leftGeometry);

        Geometry rightGeometry = getBlockQuadGeometry(Face.RIGHT);
        rightGeometry.setMaterial(grassSideMaterial);
        block.attachChild(rightGeometry);

        Geometry frontGeometry = getBlockQuadGeometry(Face.FRONT);
        frontGeometry.setMaterial(grassSideMaterial);
        block.attachChild(frontGeometry);

        Geometry backGeometry = getBlockQuadGeometry(Face.BACK);
        backGeometry.setMaterial(grassSideMaterial);
        block.attachChild(backGeometry);

        return block;
    }

    private Geometry getBlockQuadGeometry(Face face) {
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

    @Bean
    public Block cobbleStone(Material cobbleMaterial) {

        return getQuadBlock("cobble", cobbleMaterial, cobbleMaterial, cobbleMaterial);
    }

    @Bean
    public Block grass() {
        Texture grassTopTex = assetManager.loadTexture(
                "textures/grass_top.png");

        Material grassTopMaterial = new Material(assetManager,
                "matdefs/GrayScaleColorMap.j3md");
        grassTopMaterial.setTexture("TextureMap", grassTopTex);
        grassTopMaterial.setColor("Color", new ColorRGBA(0.51f, 0.83f, 0.24f, 1.0f));

        Texture grassSideTex = assetManager.loadTexture(
                "textures/bdc_grass_side01.png");
        Material grassSideMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        grassSideMaterial.setTexture("ColorMap", grassSideTex);

        Texture grassBottomTex = assetManager.loadTexture(
                "textures/bdc_dirt03.png");
        Material grassBottomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        grassBottomMaterial.setTexture("ColorMap", grassBottomTex);

        return getQuadBlock("grass", grassTopMaterial, grassSideMaterial, grassBottomMaterial);
    }

    @Bean
    public Block leaf() {
        Texture grassTopTex = assetManager.loadTexture(
                "textures/leaves_oak.png");

        Material grassBottomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        grassBottomMaterial.setTexture("ColorMap", grassTopTex);

        return getQuadBlock("treeLeaf", grassBottomMaterial, grassBottomMaterial, grassBottomMaterial);
    }

    @Bean
    public Block appleLeaf() {
        Texture grassTopTex = assetManager.loadTexture(
                "textures/leaves_apple.png");

        Material grassBottomMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        grassBottomMaterial.setTexture("ColorMap", grassTopTex);

        return getQuadBlock("appleLeaf", grassBottomMaterial, grassBottomMaterial, grassBottomMaterial);
    }

    @Bean
    public Block treeTrunk() {
        Texture treeTrunkTop = assetManager.loadTexture(
                "textures/log_oak_top.png");

        Material treeTrunkTopMaterial = new Material(assetManager,
                "matdefs/GrayScaleColorMap.j3md");
        treeTrunkTopMaterial.setTexture("TextureMap", treeTrunkTop);

        Texture treeTrunkSideTex = assetManager.loadTexture(
                "textures/log_oak.png");
        Material treeTrunkSideMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        treeTrunkSideMaterial.setTexture("ColorMap", treeTrunkSideTex);

        return getQuadBlock("treeTrunk", treeTrunkTopMaterial, treeTrunkSideMaterial, treeTrunkTopMaterial);
    }
}
