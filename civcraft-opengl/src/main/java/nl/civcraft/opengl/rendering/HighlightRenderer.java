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
 * Created by Bob on 12-7-2019.
 * <p>
 * This is probably not worth documenting
 */
public class HighlightRenderer {

    private static final float HIGH_LIGHT_SIZE = 1.01953125f;

    private final TextureManager textureManager;
    private final ObjFileManager objFileManager;
    private final Map<GameObject, Node> renderedHighlights;
    private final Node voxelHighLightsNode;
    private Material material;

    @Inject
    public HighlightRenderer(@Named("voxelHighlight") PrefabManager voxelHighlightManager,
                             TextureManager textureManager,
                             GameEngine gameEngine,
                             ObjFileManager objFileManager,
                             @Named("rootNode") Node rootNode) throws IOException {
        this.textureManager = textureManager;
        this.objFileManager = objFileManager;
        voxelHighlightManager.getGameObjectCreated().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::newHighlight));
        voxelHighlightManager.getGameObjectDestroyed().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::removedHighlight));
        voxelHighlightManager.getGameObjectChanged().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::updateHighlight));
        this.voxelHighLightsNode = new Node("voxelHighLightsNode", rootNode);
        renderedHighlights = new HashMap<>();

    }

    private void updateHighlight(GameObject gameObject) {
        removedHighlight(gameObject);
        newHighlight(gameObject);
    }

    private void removedHighlight(GameObject gameObject) {
        if (renderedHighlights.containsKey(gameObject)) {
            Node node = renderedHighlights.get(gameObject);
            voxelHighLightsNode.removeChild(node);
        }
    }

    private void newHighlight(GameObject gameObject) {
        if (material == null) {
            this.material = new Material(textureManager.loadTexture("/textures/voxelHighLight.png"));
        }
        Node gameObjectNode = new Node(voxelHighLightsNode);
        gameObjectNode.getTransform().translate(gameObject.getTransform().getTranslation(new Vector3f()));
        gameObjectNode.getTransform().scale(HIGH_LIGHT_SIZE, HIGH_LIGHT_SIZE, HIGH_LIGHT_SIZE);
        gameObjectNode.addChild(new Geometry(objFileManager.loadMesh("models/cube.obj"), material));
        gameObjectNode.setGameObject(gameObject);
        this.renderedHighlights.put(gameObject, gameObjectNode);
    }
}
