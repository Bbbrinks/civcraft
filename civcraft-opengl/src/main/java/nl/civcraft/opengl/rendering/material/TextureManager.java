package nl.civcraft.opengl.rendering.material;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bob on 13-10-2017.
 * <p>
 * This is probably not worth documenting
 */
public class TextureManager {

    private static final String TEXTURES_NOT_FOUND_PNG = "/textures/not-found.png";
    private final Map<String, Texture> textureMap = new HashMap<>();

    public Texture loadTexture(String name) {
        try {
        if(!textureMap.containsKey(name)) {
            Texture texture = new Texture(name);
            textureMap.put(name,texture);
        }
        return textureMap.get(name);
        } catch (IOException e) {
            if(!textureMap.containsKey(TEXTURES_NOT_FOUND_PNG)) {
                Texture texture = null;
                try {
                    texture = new Texture(TEXTURES_NOT_FOUND_PNG);
                } catch (IOException e1) {
                    throw new IllegalStateException("Fallback texture not found: " + TEXTURES_NOT_FOUND_PNG);
                }
                textureMap.put(TEXTURES_NOT_FOUND_PNG, texture);
            }
            return textureMap.get(TEXTURES_NOT_FOUND_PNG);
        }
    }
}
