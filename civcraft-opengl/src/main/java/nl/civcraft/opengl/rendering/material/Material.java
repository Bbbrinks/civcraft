package nl.civcraft.opengl.rendering.material;

import nl.civcraft.opengl.engine.ShaderProgram;
import nl.civcraft.opengl.rendering.ShadowMap;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class Material {

    private static final Vector4f DEFAULT_COLOUR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

    private Vector4f ambientColour;

    private Vector4f diffuseColour;

    private Vector4f specularColour;
    
    private float reflectance;

    private Texture texture;
    
    private Texture normalMap;

    public Material() {
        this.ambientColour = DEFAULT_COLOUR;
        this.diffuseColour = DEFAULT_COLOUR;
        this.specularColour = DEFAULT_COLOUR;
        this.texture = null;
        this.reflectance = 0;
    }

    public Material(Vector4f colour, float reflectance) {
        this(colour, colour, colour, null, reflectance);
    }

    public Material(Texture texture) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, 0);
    }

    public Material(Texture texture, float reflectance) {
        this(DEFAULT_COLOUR, DEFAULT_COLOUR, DEFAULT_COLOUR, texture, reflectance);
    }

    public Material(Vector4f ambientColour, Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
        this.ambientColour = ambientColour;
        this.diffuseColour = diffuseColour;
        this.specularColour = specularColour;
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientColour() {
        return ambientColour;
    }

    public void setAmbientColour(Vector4f ambientColour) {
        this.ambientColour = ambientColour;
    }

    public Vector4f getDiffuseColour() {
        return diffuseColour;
    }

    public void setDiffuseColour(Vector4f diffuseColour) {
        this.diffuseColour = diffuseColour;
    }

    public Vector4f getSpecularColour() {
        return specularColour;
    }

    public void setSpecularColour(Vector4f specularColour) {
        this.specularColour = specularColour;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public boolean hasNormalMap() {
        return this.normalMap != null;
    }

    public Texture getNormalMap() {
        return normalMap;
    }

    public void setNormalMap(Texture normalMap) {
        this.normalMap = normalMap;
    }

    public void bind(Matrix4f viewMatrix, ShaderProgram shader, ShaderProgram sceneShaderProgram, ShadowMap shadowMap) {
        if (viewMatrix != null) {
            shader.setUniform("material", this);
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getId());
        }

        if (texture != null) {
            sceneShaderProgram.setUniform("numCols", texture.getNumCols());
            sceneShaderProgram.setUniform("numRows", texture.getNumRows());
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        if ( normalMap != null ) {
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE1);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, normalMap.getId());
        }
    }
}