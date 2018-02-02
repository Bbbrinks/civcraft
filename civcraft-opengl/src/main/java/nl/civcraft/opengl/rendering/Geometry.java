package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.rendering.material.Material;

import java.util.List;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Geometry {
    protected final List<Mesh> meshes;
    protected final Material texture;

    public Geometry(List<Mesh> meshes,
                    Material material) {
        this.meshes = meshes;
        this.texture = material;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public Material getMaterial() {
        return texture;
    }

    public void render(){
        meshes.forEach(mesh -> mesh.render(texture));
    }

    public void cleanUp() {
        meshes.forEach(Mesh::cleanUp);

        // Delete the texture
        texture.cleanup();
    }
}