package nl.civcraft.opengl.rendering.light;

import org.joml.Vector3f;

import javax.inject.Singleton;

@Singleton
public class SceneLight {

    public SceneLight() {
        // Ambient Light
        setAmbientLight(new Vector3f(0.1f, 0.1f, 0.1f));
        setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 0.9f;
        Vector3f lightDirection = new Vector3f(0.1f, 0.8f, 0.8f);
        directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        directionalLight.setShadowPosMult(100);
        directionalLight.setOrthoCords(-1000000.0f, 1000000.0f, -100000.0f, 1000000.0f, -1.0f, 20000000.0f);
    }

    private Vector3f ambientLight;
    
    private Vector3f skyBoxLight;

    private PointLight[] pointLightList;
    
    private SpotLight[] spotLightList;
    
    private DirectionalLight directionalLight;

    public Vector3f getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Vector3f ambientLight) {
        this.ambientLight = ambientLight;
    }

    public PointLight[] getPointLightList() {
        return pointLightList;
    }

    public void setPointLightList(PointLight[] pointLightList) {
        this.pointLightList = pointLightList;
    }

    public SpotLight[] getSpotLightList() {
        return spotLightList;
    }

    public void setSpotLightList(SpotLight[] spotLightList) {
        this.spotLightList = spotLightList;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }

    public Vector3f getSkyBoxLight() {
        return skyBoxLight;
    }

    public void setSkyBoxLight(Vector3f skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }
    
}