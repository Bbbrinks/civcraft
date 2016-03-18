package nl.civcraft.core.conf;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import nl.civcraft.core.managers.NpcManager;
import nl.civcraft.core.managers.TaskManager;
import nl.civcraft.core.npc.Npc;
import nl.civcraft.core.pathfinding.AStarPathFinder;
import nl.civcraft.core.rendering.CivvyRenderer;
import nl.civcraft.core.tasks.Wander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CivvyConfiguration {

    @Autowired
    private AssetManager assetManager;
    @Autowired
    private AStarPathFinder pathFinder;

    @Bean
    public Npc civvy() {
        Material cobbleMaterial = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        cobbleMaterial.setColor("Color", ColorRGBA.Blue);
        return createSingleGeometryBoxCivvy(cobbleMaterial, "civvy");
    }

    private Npc createSingleGeometryBoxCivvy(Material cobbleMaterial, String name) {
        Box box = new Box(0.25f, 2.0f, 0.35f);
        Geometry geometry = new Geometry("box", box);
        geometry.setMaterial(cobbleMaterial);
        geometry.setLocalTranslation(0.0f, 1.0f, 0.0f);
        Npc block = new Npc(name);
        block.attachChild(geometry);
        block.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        return block;
    }

    @Bean
    public NpcManager npcManager() {
        return new NpcManager();
    }

    @Bean
    public TaskManager taskManager() {
        TaskManager taskManager = new TaskManager();
        taskManager.addTask(new Wander(pathFinder));
        return taskManager;
    }

    @Bean
    public CivvyRenderer civvyRenderer(Node rootNode) {
        return new CivvyRenderer(rootNode);
    }

}
