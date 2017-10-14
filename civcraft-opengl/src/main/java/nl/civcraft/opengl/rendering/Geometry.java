package nl.civcraft.opengl.rendering;

/**
 * Created by Bob on 14-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Geometry {
    private final Mesh mesh;
    private final Texture texture;

    public Geometry(Mesh mesh,
                    Texture texture) {
        this.mesh = mesh;
        this.texture = texture;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void render(){
        mesh.render(texture);
    }

    public void cleanUp() {
        mesh.cleanUp();

        // Delete the texture
        texture.cleanup();
    }
}
