package nl.civcraft.opengl.interaction;

import nl.civcraft.core.gamecomponents.Voxel;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.opengl.raycast.MousePicker;
import nl.civcraft.opengl.rendering.Box;
import nl.civcraft.opengl.rendering.Geometry;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.rendering.material.Material;
import nl.civcraft.opengl.rendering.material.TextureManager;
import org.apache.logging.log4j.LogManager;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Optional;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class CurrentVoxelHighlighter implements nl.civcraft.core.interaction.util.CurrentVoxelHighlighter {
    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    public static final float HIGH_LIGHT_SIZE = 1.01953125f;

    private final MousePicker mousePicker;
    private final Node voxelHighLightsNode;
    private final TextureManager textureManager;
    private Material material;

    @Inject
    public CurrentVoxelHighlighter(MousePicker mousePicker,
                                   @Named("rootNode") Node rootNode,
                                   TextureManager textureManager) {
        this.mousePicker = mousePicker;
        this.voxelHighLightsNode = new Node("voxelHighLightsNode", rootNode);

        this.textureManager = textureManager;
    }

    @Override
    public GameObject highLight() {
        Optional<GameObject> currentVoxel = getCurrentVoxel();
        if(currentVoxel.isPresent()) {
            this.clear();
            if(material == null){
                this.material = new Material(textureManager.loadTexture("/textures/voxelHighLight.png"));
            }

            GameObject gameObject = currentVoxel.get();
            Node gameObjectNode = new Node(voxelHighLightsNode);
            gameObjectNode.getTransform().translate(gameObject.getTransform().getTranslation(new Vector3f()));
            gameObjectNode.getTransform().scale(HIGH_LIGHT_SIZE, HIGH_LIGHT_SIZE, HIGH_LIGHT_SIZE);
            gameObjectNode.addChild(new Geometry(Collections.singletonList(Box.instance()), material));
            gameObjectNode.setGameObject(gameObject);
            return gameObject;
        }
        return null;
    }

    @Override
    public Optional<GameObject> getCurrentVoxel() {
        return mousePicker.pickNearest(Voxel.class);
    }

    @Override
    public void clear() {
        voxelHighLightsNode.detachAll();
    }
}
