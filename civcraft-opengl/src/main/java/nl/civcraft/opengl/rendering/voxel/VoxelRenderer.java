package nl.civcraft.opengl.rendering.voxel;

import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.managers.PrefabManager;
import nl.civcraft.core.managers.TickManager;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.core.model.NeighbourDirection;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.rendering.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.*;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class VoxelRenderer {


    private final Node voxels;
    private final TextureManager textureManager;
    private final Map<GameObject, Node> renderedVoxels;


    @Inject
    public VoxelRenderer(@Named("block") PrefabManager blockManager,
                         @Named("rootNode") Node rootNode,
                         TextureManager textureManager,
                         TickManager tickManager,
                         Window window) throws IOException {
        this.textureManager = textureManager;
        blockManager.getGameObjectCreated().subscribe(this::newVoxel);
        blockManager.getGameObjectDestroyed().subscribe(this::removedVoxel);
        blockManager.getGameObjectChanged().subscribe(this::newVoxel);
        renderedVoxels = new HashMap<>();
        voxels = new Node("voxels", rootNode);


    }


    private void removedVoxel(GameObject gameObject) {
        if (renderedVoxels.containsKey(gameObject)) {
            Node node = renderedVoxels.get(gameObject);
            voxels.removeChild(node);
            renderedVoxels.remove(gameObject);
        }
    }

    private void newVoxel(GameObject gameObject) {
        updateVoxel(gameObject);
    }

    private void updateVoxel(GameObject gameObject) {
        Voxel voxel = gameObject.getComponent(Voxel.class).orElseThrow(() -> new IllegalStateException("not a voxel"));
        if (voxel.isVisible() && !renderedVoxels.containsKey(gameObject)) {
            Node gameObjectNode = new Node(voxels);

            gameObjectNode.getTransform().add(gameObject.getTransform());


            gameObjectNode.getGeometries().add(() -> {
                Texture texture = textureManager.loadTexture(String.format("/textures/%s.png", voxel.getType()));
                List<Mesh> quads = new ArrayList<>();
                if (!voxel.getNeighbour(NeighbourDirection.FRONT).isPresent()) {
                    quads.add(Quad.front());
                }
                if (!voxel.getNeighbour(NeighbourDirection.BACK).isPresent()) {
                    quads.add(Quad.back());
                }
                if (!voxel.getNeighbour(NeighbourDirection.TOP).isPresent()) {
                    quads.add(Quad.top());
                }
                if (!voxel.getNeighbour(NeighbourDirection.BOTTOM).isPresent()) {
                    quads.add(Quad.bottom());
                }
                if (!voxel.getNeighbour(NeighbourDirection.LEFT).isPresent()) {
                    quads.add(Quad.left());
                }
                if (!voxel.getNeighbour(NeighbourDirection.RIGHT).isPresent()) {
                    quads.add(Quad.right());
                }
                return new Geometry(quads, texture);
            });
            renderedVoxels.put(gameObject, gameObjectNode);
        } else if (voxel.isVisible()) {
            Node node = renderedVoxels.get(gameObject);
            voxels.removeChild(node);
            renderedVoxels.remove(gameObject);
            updateVoxel(gameObject);
        } else {
            if (renderedVoxels.containsKey(gameObject)) {
                Node node = renderedVoxels.get(gameObject);
                voxels.removeChild(node);
                renderedVoxels.remove(gameObject);
            }
        }
    }
}
