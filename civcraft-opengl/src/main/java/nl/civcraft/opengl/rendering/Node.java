package nl.civcraft.opengl.rendering;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Node {
    private final List<Node> children;
    private final List<Supplier<Geometry>> geometries;
    private final Node parent;
    private final String name;
    private final Matrix4f transform;

    public Node(Node parent) {
        this(UUID.randomUUID().toString(), parent);
    }

    public Node(String name,  Node parent) {
        this.geometries = new ArrayList<>();
        children = new ArrayList<>();
        this.parent = parent;
        transform = new Matrix4f();
        this.name = name;
        if(parent != null) {
            parent.getChildren().add(this);
        }
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Supplier<Geometry>> getGeometries() {
        return geometries;
    }

    public Matrix4f getTransform() {
        return transform;
    }

    public void removeChild(Node node) {
        children.remove(node);
    }

    public Node getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }
}
