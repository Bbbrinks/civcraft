package nl.civcraft.opengl.rendering;

import nl.civcraft.core.gamecomponents.Voxel;
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
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelRenderer {


    private final TextureManager textureManager;
    private final Map<GameObject, Node> renderedVoxels;
    private final ChunkManager chunkManager;
    private final ObjFileManager objFileManager;


    @Inject
    public VoxelRenderer(@Named("block") PrefabManager blockManager,
                         TextureManager textureManager,
                         GameEngine gameEngine,
                         ChunkManager chunkManager,
                         ObjFileManager objFileManager) throws IOException {
        this.textureManager = textureManager;
        this.chunkManager = chunkManager;
        this.objFileManager = objFileManager;
        blockManager.getGameObjectCreated().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::newVoxel));
        blockManager.getGameObjectDestroyed().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::removedVoxel));
        blockManager.getGameObjectChanged().buffer(gameEngine.getUpdateScene()).subscribe(gameObjects -> gameObjects.forEach(this::updateVoxel));
        renderedVoxels = new HashMap<>();

    }


    private void removedVoxel(GameObject gameObject) {
        if (renderedVoxels.containsKey(gameObject)) {
            Node node = renderedVoxels.get(gameObject);
            chunkManager.getOrCreateChunk(gameObject.getTransform().getTranslation(new Vector3f())).removeChild(node);
            renderedVoxels.remove(gameObject);
        }
    }

    private void newVoxel(GameObject gameObject) {
        updateVoxel(gameObject);
    }

    private void updateVoxel(GameObject gameObject) {
        Voxel voxel = gameObject.getComponent(Voxel.class).orElseThrow(() -> new IllegalStateException("not a voxel"));
        Node chunkNode = chunkManager.getOrCreateChunk(gameObject.getTransform().getTranslation(new Vector3f()));
        if (voxel.isVisible() && !renderedVoxels.containsKey(gameObject)) {
            Node gameObjectNode = new Node(chunkNode);

            gameObjectNode.getTransform().translate(gameObject.getTransform().getTranslation(new Vector3f()));

            Material material = new Material(textureManager.loadTexture(String.format("/textures/%s.png", voxel.getType())));
            gameObjectNode.addChild(new Geometry(objFileManager.loadMesh("models/cube.obj"), material));
            gameObjectNode.setGameObject(gameObject);
            renderedVoxels.put(gameObject, gameObjectNode);
        } else if (voxel.isVisible()) {
            Node node = renderedVoxels.get(gameObject);
            chunkNode.removeChild(node);
            renderedVoxels.remove(gameObject);
            updateVoxel(gameObject);
        } else {
            if (renderedVoxels.containsKey(gameObject)) {
                Node node = renderedVoxels.get(gameObject);
                chunkNode.removeChild(node);
                renderedVoxels.remove(gameObject);
            }
        }
    }
}
