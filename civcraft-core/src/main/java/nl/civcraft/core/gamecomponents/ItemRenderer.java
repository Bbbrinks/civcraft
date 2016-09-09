package nl.civcraft.core.gamecomponents;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import nl.civcraft.core.managers.Physical;
import nl.civcraft.core.model.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */

@Component
public class ItemRenderer implements GameComponent {

    private final Node entityNode;
    private final AssetManager assetManager;

    @Autowired
    public ItemRenderer(Node rootNode, AssetManager assetManager) {
        this.assetManager = assetManager;
        entityNode = new Node("entityNode");
        rootNode.attachChild(entityNode);
    }

    @Override
    public void addTo(GameObject gameObject) {
        Optional<ItemComponent> itemComponent = gameObject.getComponent(ItemComponent.class);
        if (!itemComponent.isPresent()) {
            throw new IllegalStateException("ItemRenderer can only be applied with ItemComponent");
        }
        Quad quad = new Quad(0.5f, 0.5f);

        Geometry item = new Geometry("item", quad);
        Material mat = new Material(assetManager,  // Create new material and...
                "Common/MatDefs/Misc/Unshaded.j3md");  // ... specify .j3md file to use (unshaded).
        mat.setColor("Color", ColorRGBA.Brown);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        item.setMaterial(mat);
        Optional<Physical> physical = gameObject.getComponent(Physical.class);
        if (!physical.isPresent()) {
            throw new IllegalStateException("ItemRenderer can only be applied with Physical");
        }
        item.setLocalTransform(physical.get().getTransform());
        entityNode.attachChild(item);
    }
}
