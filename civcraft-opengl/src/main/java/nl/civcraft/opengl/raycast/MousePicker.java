package nl.civcraft.opengl.raycast;

import nl.civcraft.core.gamecomponents.GameComponent;
import nl.civcraft.core.interaction.MouseInputManagerInterface;
import nl.civcraft.core.model.GameObject;
import nl.civcraft.opengl.engine.Window;
import nl.civcraft.opengl.rendering.Camera;
import nl.civcraft.opengl.rendering.Node;
import nl.civcraft.opengl.util.MatrixUtil;
import org.apache.logging.log4j.LogManager;
import org.joml.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Bob on 12-1-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class MousePicker {

    private static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger();
    private final MouseInputManagerInterface mouseInputManagerInterface;
    private final Window window;
    private final Camera camera;
    private final Node rootNode;


    @Inject
    public MousePicker(MouseInputManagerInterface mouseInputManagerInterface,
                       Window window,
                       Camera camera,
                       @Named("rootNode") Node rootNode) {
        this.mouseInputManagerInterface = mouseInputManagerInterface;
        this.window = window;
        this.camera = camera;
        this.rootNode = rootNode;
    }

    public List<Node> pick() {
        Vector3f mouseDir = getMouseDirection();

        List<Node> intersections = new ArrayList<>();

        intersect(mouseDir, rootNode, intersections);

        return intersections;
    }

    public <T extends GameComponent> Optional<GameObject> pickNearest(Class<T> gameComponent) {
        return pick().stream().filter(node -> {
            GameObject gameObject = node.getGameObject();
            return gameObject != null && gameObject.hasComponent(gameComponent);
        }).map(Node::getGameObject)
                .sorted((o1, o2) -> Float.compare(o1.getTransform().getTranslation(new Vector3f()).distance(camera.getPosition()), o2.getTransform().getTranslation(new Vector3f())
                        .distance(camera.getPosition()))).findFirst();

    }

    public Vector3f getMouseDirection() {
        Vector2d mousePos = mouseInputManagerInterface.getMousePosition();

        int wdwWitdh = window.getWidth();
        int wdwHeight = window.getHeight();

        float x = 2 * (float) mousePos.x / (float) wdwWitdh - 1.0f;
        float y = 1.0f - 2 * (float) mousePos.y / (float) wdwHeight;
        float z = -1.0f;

        Matrix4f invProjectionMatrix = MatrixUtil.getProjectionMatrix(window).invert(new Matrix4f());
        Vector4f tmpVec = new Vector4f(x, y, z, 1.0f);
        tmpVec.mul(invProjectionMatrix);
        tmpVec.z = -1.0f;
        tmpVec.w = 0.0f;

        Matrix4f invViewMatrix = MatrixUtil.getViewMatrix(camera).invert(new Matrix4f());
        tmpVec.mul(invViewMatrix);

        return new Vector3f(tmpVec.x, tmpVec.y, tmpVec.z);
    }

    private void intersect(Vector3f dir,
                           Node node,
                           List<Node> intersections) {
        float closestDistance = Float.POSITIVE_INFINITY;
        Vector2f nearFar = new Vector2f();
        AABBf boundingBox = node.getBoundingBox();
        Vector3f min = new Vector3f(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        Vector3f max = new Vector3f(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        if (Intersectionf.intersectRayAab(camera.getPosition(), dir, min, max, nearFar) && nearFar.x < closestDistance) {
            LOGGER.trace("Node intersected " + node.getName());
            intersections.add(node);
            node.getChildren().forEachRemaining(child -> intersect(dir, child, intersections));
        }
    }
}
