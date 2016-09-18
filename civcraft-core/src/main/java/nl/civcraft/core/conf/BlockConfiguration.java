package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import nl.civcraft.core.model.Block;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.utils.BlockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlockConfiguration {

    public static final String UNSHADED_MAT_DEF = "Common/MatDefs/Misc/Unshaded.j3md";
    public static final String COLOR_MAP = "ColorMap";
    @Autowired
    private AssetManager assetManager;


    @Bean
    public Block dirt(Material dirtMaterial) {
        return BlockUtil.getQuadBlock("dirt", dirtMaterial, dirtMaterial, dirtMaterial);
    }

    @Bean
    public Block cobbleStone(Material cobbleMaterial) {
        return BlockUtil.getQuadBlock("cobble", cobbleMaterial, cobbleMaterial, cobbleMaterial);
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
                UNSHADED_MAT_DEF);
        grassSideMaterial.setTexture(COLOR_MAP, grassSideTex);

        Texture grassBottomTex = assetManager.loadTexture(
                "textures/bdc_dirt03.png");
        Material grassBottomMaterial = new Material(assetManager,
                UNSHADED_MAT_DEF);
        grassBottomMaterial.setTexture(COLOR_MAP, grassBottomTex);

        return BlockUtil.getQuadBlock("grass", grassTopMaterial, grassSideMaterial, grassBottomMaterial);
    }

    @Bean
    public Block leaf() {
        Texture grassTopTex = assetManager.loadTexture(
                "textures/leaves_oak.png");

        Material grassBottomMaterial = new Material(assetManager,
                UNSHADED_MAT_DEF);
        grassBottomMaterial.setTexture(COLOR_MAP, grassTopTex);

        return BlockUtil.getQuadBlock("treeLeaf", grassBottomMaterial, grassBottomMaterial, grassBottomMaterial);
    }

    @Bean
    public Block appleLeaf() {
        Texture grassTopTex = assetManager.loadTexture(
                "textures/leaves_apple.png");

        Material grassBottomMaterial = new Material(assetManager,
                UNSHADED_MAT_DEF);
        grassBottomMaterial.setTexture(COLOR_MAP, grassTopTex);

        return BlockUtil.getQuadBlock("appleLeaf", grassBottomMaterial, grassBottomMaterial, grassBottomMaterial);
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
                UNSHADED_MAT_DEF);
        treeTrunkSideMaterial.setTexture(COLOR_MAP, treeTrunkSideTex);

        return BlockUtil.getQuadBlock("treeTrunk", treeTrunkTopMaterial, treeTrunkSideMaterial, treeTrunkTopMaterial);
    }

    @Bean
    public Spatial stockpileSpatial(AssetManager assetManager) {
        Node stockpile = new Node();
        Geometry topGeometry = BlockUtil.getBlockQuadGeometry(Face.TOP);
        topGeometry.setLocalTranslation(topGeometry.getLocalTranslation().add(new Vector3f(0, 0.001f, 0)));
        Material mat = new Material(assetManager,  // Create new material and...
                UNSHADED_MAT_DEF);  // ... specify .j3md file to use (unshaded).
        mat.setColor("Color", new ColorRGBA(0.3f, 0.9f, 0.9f, 0.5f));
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        topGeometry.setMaterial(mat);
        stockpile.attachChild(topGeometry);
        return stockpile;
    }
}
