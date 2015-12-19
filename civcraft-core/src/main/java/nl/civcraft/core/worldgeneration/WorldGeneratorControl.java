package nl.civcraft.core.worldgeneration;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;

public class WorldGeneratorControl extends AbstractControl {

    private final WorldGenerator worldGenerator;
    private final Node rootNode;

    public WorldGeneratorControl(WorldGenerator worldGenerator, Node rootNode) {
        this.worldGenerator = worldGenerator;
        this.rootNode = rootNode;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (worldGenerator.isGenerationDone()) {
            rootNode.detachAllChildren();
            worldGenerator.getChunks().forEach(rootNode::attachChild);
            worldGenerator.setGenerationDone(false);
            rootNode.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
