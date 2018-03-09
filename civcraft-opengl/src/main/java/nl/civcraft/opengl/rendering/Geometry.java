package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.engine.ShaderProgram;
import nl.civcraft.opengl.rendering.material.Material;
import org.joml.AABBf;
import org.joml.Matrix4f;

import java.util.List;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Geometry {
    protected final List<Mesh> meshes;
    protected final Material material;
    private AABBf boundingBox;

    public Geometry(List<Mesh> meshes,
                    Material material) {
        this.meshes = meshes;
        this.material = material;
        updateBounds();
    }

    public void updateBounds() {
        AABBf bounds = new AABBf();
        for (Mesh mesh : meshes) {
            bounds = bounds.union(mesh.getBounds(), new AABBf());
        }
        boundingBox = bounds;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public Material getMaterial() {
        return material;
    }

    public void render(Matrix4f viewMatrix,
                       ShaderProgram shader,
                       ShaderProgram sceneShaderProgram,
                       ShadowMap shadowMap) {
        meshes.forEach(mesh -> mesh.render(material, viewMatrix, shader, sceneShaderProgram, shadowMap));
    }

    public void cleanUp() {
        meshes.forEach(Mesh::cleanUp);

    }

    public AABBf getBoundingBox() {
        return boundingBox;
    }

}
