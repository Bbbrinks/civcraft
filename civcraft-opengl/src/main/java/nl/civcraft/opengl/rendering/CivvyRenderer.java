package nl.civcraft.opengl.rendering;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.opengl.engine.GameEngine;
import nl.civcraft.opengl.rendering.geometry.Box;
import nl.civcraft.opengl.rendering.material.TextureManager;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 12-1-2018.
 * <p>
 * This is probably not worth documenting
 */
public class CivvyRenderer {


    private final Map<GameObject, Node> renderedCivvies;
    private final TextureManager textureManager;
    private final Node civvies;

    @Inject
    public CivvyRenderer(@Named("civvy") PrefabManager civvyManager,
                         @Named("rootNode") Node rootNode,
                         TextureManager textureManager,
                         GameEngine gameEngine) throws IOException {
        this.textureManager = textureManager;


        civvyManager.getGameObjectCreated().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::newCivvy));
        civvyManager.getGameObjectDestroyed().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::removedCivvy));
        civvyManager.getGameObjectChanged().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::updatedCivvy));
        renderedCivvies = new HashMap<>();
        civvies = new Node("civvies", rootNode);


    }

    private void updatedCivvy(GameObject gameObject) {
        removedCivvy(gameObject);
        Node gameObjectNode = new Node(civvies);
        gameObjectNode.getTransform().add(gameObject.getTransform());
        gameObjectNode.getTransform().scale(0.5f, 2, 0.5f);
        gameObjectNode.getGeometries().add(() -> new Box(textureManager.loadTexture("/textures/blue.png")));
        renderedCivvies.put(gameObject, gameObjectNode);
    }

    private void removedCivvy(GameObject gameObject) {
        if (renderedCivvies.containsKey(gameObject)) {
            Node node = renderedCivvies.get(gameObject);
            civvies.removeChild(node);
            renderedCivvies.remove(gameObject);
        }
    }

    private void newCivvy(GameObject gameObject) {
        updatedCivvy(gameObject);
    }
}
