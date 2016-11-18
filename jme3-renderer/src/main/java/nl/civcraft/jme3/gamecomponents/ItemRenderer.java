package nl.civcraft.jme3.gamecomponents;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import nl.civcraft.core.gamecomponents.AbstractGameComponent;
import nl.civcraft.core.gamecomponents.ItemComponent;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Bob on 9-9-2016.
 * <p>
 * This is probably not worth documenting
 */

@Component
public class ItemRenderer extends AbstractGameComponent {

    private final Node entityNode;
    private final AssetManager assetManager;
    private final Map<Item, Geometry> renderedItems;

    @Autowired
    public ItemRenderer(Node rootNode, AssetManager assetManager) {
        this.assetManager = assetManager;
        entityNode = new Node("entityNode");
        rootNode.attachChild(entityNode);
        renderedItems = new HashMap<>();
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
        item.setLocalTransform(gameObject.getTransform());
        entityNode.attachChild(item);
        renderedItems.put(itemComponent.get().getItem(), item);
        super.addTo(gameObject);
    }

    @Override
    public void destroyed(GameObject gameObject) {
        Item item = gameObject.getComponent(ItemComponent.class).get().getItem();
        entityNode.detachChild(renderedItems.remove(item));
        super.destroyed(gameObject);
    }
}
