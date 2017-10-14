package nl.civcraft.opengl.rendering;

import java.util.List;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Geometry {
    private final List<Mesh> meshes;
    private final Texture texture;

    public Geometry(List<Mesh> meshes,
                    Texture texture) {
        this.meshes = meshes;
        this.texture = texture;
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    public Texture getTexture() {
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
