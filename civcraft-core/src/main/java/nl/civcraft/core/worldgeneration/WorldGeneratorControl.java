package nl.civcraft.core.worldgeneration;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.Chunk;

public class WorldGeneratorControl extends AbstractControl {


    private final Node voxels;
    private WorldManager worldManager;

    private final WorldGenerator worldGenerator;

    public WorldGeneratorControl(WorldManager worldManager, WorldGenerator worldGenerator, Node rootNode) {
        this.worldGenerator = worldGenerator;
        voxels = new Node("voxels");
        rootNode.attachChild(voxels);
        this.worldManager = worldManager;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (worldGenerator.isGenerationDone()) {
            voxels.detachAllChildren();
            worldManager.getWorld().clearChunks();
            worldGenerator.getChunks().forEach(voxels::attachChild);
            for (Chunk chunk : worldGenerator.getChunks()) {
                worldManager.getWorld().addChunk(chunk);
            }

            worldGenerator.setGenerationDone(false);
            voxels.removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
