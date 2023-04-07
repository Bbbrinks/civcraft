package nl.civcraft.jme3.nl.civcraft.jme3.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import nl.civcraft.core.model.Face;
import nl.civcraft.jme3.nl.civcraft.jme3.Civcraft;
import nl.civcraft.jme3.nl.civcraft.jme3.utils.BlockUtil;
import nl.civcraft.jme3.nl.civcraft.jme3.utils.MaterialUtil;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class SpatialsModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Named("moveToSpatial")
    @Singleton
    public Spatial moveToSpacial(AssetManager assetManager) {
        return BlockUtil.getColoredBlock(assetManager, new ColorRGBA(0.5f, 0.3f, 0.2f, 0.5f));
    }

    @Provides
    @Named("hoverSpatial")
    @Singleton
    public Spatial hoverSpatial(AssetManager assetManager) {
        return BlockUtil.getColoredBlock(assetManager, new ColorRGBA(0.7f, 0.7f, 0.1f, 0.5f));
    }

    @Provides
    @Named("stockpileSpatial")
    @Singleton
    public Spatial stockpileSpatial(AssetManager assetManager) {
        Node stockpile = new Node();
        Geometry topGeometry = BlockUtil.getBlockQuadGeometry(Face.TOP);
        topGeometry.setLocalTranslation(topGeometry.getLocalTranslation().add(new Vector3f(0, 0.001f, 0)));
        Material mat = MaterialUtil.getColoredMaterial(assetManager, new ColorRGBA(0.3f, 0.9f, 0.9f, 0.5f));
        topGeometry.setMaterial(mat);
        stockpile.attachChild(topGeometry);
        return stockpile;
    }

    @Provides
    @Named("civvy")
    @Singleton
    public Node civvy(AssetManager assetManager) {
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setColor("Color", ColorRGBA.Blue);
        return BlockUtil.createSingleGeometryBoxCivvy(cobbleMaterial);
    }

    @Provides
    @Named("selectionBoxes")
    @Singleton
    public Node selectionBoxes(Civcraft civcraft) {
        Node selectionBoxes = new Node("selectionBoxes");

        civcraft.getRootNode().attachChild(selectionBoxes);
        return selectionBoxes;
    }

    @Provides
    @Named("otherSelection")
    @Singleton
    public Node otherSelection(Civcraft civcraft) {
        Node selectionBoxes = new Node("otherSelection");

        civcraft.getRootNode().attachChild(selectionBoxes);
        return selectionBoxes;
    }
}
