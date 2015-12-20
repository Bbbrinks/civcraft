package nl.civcraft.core.worldgeneration;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Chunk;

public class WorldGeneratorControl extends AbstractControl {


    private WorldManager worldManager;

    private final WorldGenerator worldGenerator;
    private final Node rootNode;

    public WorldGeneratorControl(WorldManager worldManager, WorldGenerator worldGenerator, Node rootNode) {
        this.worldGenerator = worldGenerator;
        this.rootNode = rootNode;
        this.worldManager = worldManager;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (worldGenerator.isGenerationDone()) {
            rootNode.detachAllChildren();
            worldManager.getWorld().clearChunks();
            worldGenerator.getChunks().forEach(rootNode::attachChild);
            for (Chunk chunk : worldGenerator.getChunks()) {
                worldManager.getWorld().addChunk(chunk);
            }

            worldGenerator.setGenerationDone(false);
            rootNode.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
