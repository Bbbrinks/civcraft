package nl.civcraft.jme3.nl.civcraft.jme3;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.shape.Quad;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.rendering.ItemRenderer;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class ItemRendererControl extends AbstractControl {


    private final AssetManager assetManager;
    private final Map<ItemRenderer, Geometry> renderedItems;
    private final Queue<AbstractMap.SimpleImmutableEntry<ItemRenderer, Geometry>> newItems;
    private final Queue<ItemRenderer> removedItems;
    private final Node itemNode;

    @Inject
    public ItemRendererControl(AssetManager assetManager,
                               Node rootNode,
                               @Named("item") PrefabManager prefabManager) {
        prefabManager.getGameObjectCreated().subscribe(this::handleItemCreated);
        prefabManager.getGameObjectChangedEvent().subscribe(this::handleItemChanged);
        prefabManager.getGameObjectDestroyed().subscribe(this::handleItemRemoved);

        this.assetManager = assetManager;
        renderedItems = new HashMap<>();
        newItems = new LinkedBlockingQueue<>();
        removedItems = new LinkedBlockingQueue<>();
        itemNode = new Node("itemNode");
        rootNode.attachChild(itemNode);
        rootNode.addControl(this);
    }

    public void handleItemCreated(GameObject gameObject) {
        Optional<ItemRenderer> itemComponent = gameObject.getComponent(ItemRenderer.class);
        if (!itemComponent.isPresent()) {
            return;
        }
        if (itemComponent.get().isVisible()) {
            addNewItem(gameObject, itemComponent);
        }
    }

    private void addNewItem(GameObject gameObject,
                            Optional<ItemRenderer> itemComponent) {
        Quad quad = new Quad(0.5f, 0.5f);
        Geometry item = new Geometry("item", quad);
        Material mat = new Material(assetManager,  // Create new material and...
                "Common/MatDefs/Misc/Unshaded.j3md");  // ... specify .j3md file to use (unshaded).
        mat.setColor("Color", ColorRGBA.Brown);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        item.setMaterial(mat);
        item.setLocalTransform(gameObject.getTransform());
        newItems.add(new AbstractMap.SimpleImmutableEntry<>(itemComponent.get(), item));
    }

    public void handleItemChanged(GameObject gameObject) {
        Optional<ItemRenderer> itemComponent = gameObject.getComponent(ItemRenderer.class);
        if (!itemComponent.isPresent()) {
            return;
        }
        if (renderedItems.containsKey(itemComponent.get())) {
            removedItems.add(itemComponent.get());
        }
        if (itemComponent.get().isVisible()) {
            addNewItem(gameObject, itemComponent);
        }
    }

    public void handleItemRemoved(GameObject gameObject) {
        Optional<ItemRenderer> itemComponent = gameObject.getComponent(ItemRenderer.class);
        if (!itemComponent.isPresent()) {
            return;
        }
        removedItems.add(itemComponent.get());
    }

    @Override
    protected void controlUpdate(float tpf) {
        while (!newItems.isEmpty()) {
            AbstractMap.SimpleImmutableEntry<ItemRenderer, Geometry> poll = newItems.poll();
            renderedItems.put(poll.getKey(), poll.getValue());
        }

        while (!removedItems.isEmpty()) {
            ItemRenderer poll = removedItems.poll();
            if (renderedItems.containsKey(poll)) {
                renderedItems.remove(poll);
            }
        }
        itemNode.detachAllChildren();
        for (Geometry geometry : renderedItems.values()) {
            itemNode.attachChild(geometry);
        }
    }

    @Override
    protected void controlRender(RenderManager rm,
                                 ViewPort vp) {

    }
}