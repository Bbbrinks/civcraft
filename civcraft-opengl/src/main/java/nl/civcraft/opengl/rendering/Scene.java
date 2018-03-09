package nl.civcraft.opengl.rendering;

import nl.civcraft.opengl.rendering.light.SceneLight;
import nl.civcraft.opengl.weather.Fog;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by Bob on 9-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class Scene {

    private final Node rootNode;
    private final SceneLight sceneLight;
    private final Fog fog;

    @Inject
    public Scene(@Named("rootNode") Node rootNode,
                 SceneLight sceneLight) {
        this.rootNode = rootNode;
        this.sceneLight = sceneLight;
        Vector3f fogColour = new Vector3f(0.2f, 0.2f, 0.1f);
        this.fog = new Fog(true, fogColour, 0.01f);
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public Fog getFog() {
        return fog;
    }
}
