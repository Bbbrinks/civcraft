package nl.civcraft.opengl.rendering;

import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.opengl.engine.GameEngine;
import nl.civcraft.opengl.loaders.ObjFileManager;
import nl.civcraft.opengl.rendering.material.Material;
import nl.civcraft.opengl.rendering.material.TextureManager;
import org.joml.Vector3f;

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
    private final ObjFileManager objFileManager;

    @Inject
    public CivvyRenderer(@Named("civvy") PrefabManager civvyManager,
                         @Named("rootNode") Node rootNode,
                         TextureManager textureManager,
                         GameEngine gameEngine,
                         ObjFileManager objFileManager) throws IOException {
        this.textureManager = textureManager;
        this.objFileManager = objFileManager;


        civvyManager.getGameObjectCreated().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::newCivvy));
        civvyManager.getGameObjectDestroyed().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::removedCivvy));
        civvyManager.getGameObjectChanged().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::updatedCivvy));
        renderedCivvies = new HashMap<>();
        civvies = new Node("civvies", rootNode);


    }

    private void updatedCivvy(GameObject gameObject) {
        removedCivvy(gameObject);
        Node gameObjectNode = new Node(civvies);
        gameObjectNode.getTransform().translate(gameObject.getTransform().getTranslation(new Vector3f()));
        gameObjectNode.getTransform().scale(0.5f, 1.5f, 0.5f);
        Material material = new Material(textureManager.loadTexture("/textures/blue.png"));
        gameObjectNode.addChild(new Geometry(objFileManager.loadMesh("models/cube.obj"), material));
        gameObjectNode.setGameObject(gameObject);
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
