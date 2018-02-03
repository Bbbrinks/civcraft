package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.util.MatrixUtil;
import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Node {
    private final List<Node> children;
    private final List<Geometry> geometries;
    private Node parent;
    private final String name;
    private final Matrix4f transform;
    private AABBf boundingBox;
    private boolean updateBounds = true;

    public Node(Node parent) {
        this(UUID.randomUUID().toString(), parent);
    }

    public Node(String name,
                Node parent) {
        boundingBox = new AABBf(new Vector3f(), new Vector3f());
        this.geometries = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = parent;
        transform = new Matrix4f();
        this.name = name;

        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Node node) {
        children.add(node);
        node.parent = this;
        setUpdateBounds(true);
    }

    private void setUpdateBounds(boolean updateBounds) {
        this.updateBounds = updateBounds;
    }

    public void addChild(Geometry geometry) {
        geometries.add(geometry);
        setUpdateBounds(true);
    }

    public Iterator<Node> getChildren() {
        return children.iterator();
    }

    public Iterator<Geometry> getGeometries() {
        return geometries.iterator();
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public void removeChild(Node node) {
        children.remove(node);
        setUpdateBounds(true);
    }

    public Node getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }


    public void recalculateBoundingBox() {
        if(!updateBounds) {
            return;
        }
        AABBf bounds = new AABBf(transform.getTranslation(new Vector3f()), transform.getTranslation(new Vector3f()));
        for (Node node : children) {
            bounds = bounds.union(node.getBoundingBox(), new AABBf());
        }

        for (Geometry geometry : geometries) {
            bounds = bounds.union(MatrixUtil.moveAndScaleBoundingBox(geometry.getBoundingBox(), transform), new AABBf());
        }
        this.boundingBox = bounds;
        updateBounds = false;
        if(parent != null) {
            parent.setUpdateBounds(true);
        }
    }

    public AABBf getBoundingBox() {
        return boundingBox;
    }

    public Optional<Node> getChild(Vector3f includingCoordinates) {
        return children.stream().filter(node -> node.getBoundingBox().testPoint(includingCoordinates)).findFirst();
    }

    public void detachAll() {
        children.clear();
        geometries.clear();
        setUpdateBounds(true);
    }
}
