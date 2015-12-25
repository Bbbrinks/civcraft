package nl.civcraft.core.worldgeneration;


import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.control.AbstractControl;
import nl.civcraft.core.managers.WorldManager;
import nl.civcraft.core.model.World;

public class WorldGeneratorControl extends AbstractControl {


    private final Node voxels;
    private WorldManager worldManager;

    private final WorldGenerator worldGenerator;

    public WorldGeneratorControl(WorldManager worldManager, WorldGenerator worldGenerator, Node rootNode) {
        this.worldGenerator = worldGenerator;
        rootNode.detachChildNamed("voxels");
        voxels = new Node("voxels");
        rootNode.attachChild(voxels);
        this.worldManager = worldManager;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (worldGenerator.isGenerationDone()) {
            voxels.detachAllChildren();
            World world = worldGenerator.getWorld();
            world.getChunks().forEach(voxels::attachChild);
            worldGenerator.setGenerationDone(false);
            voxels.getParent().removeControl(this);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {

    }
}
