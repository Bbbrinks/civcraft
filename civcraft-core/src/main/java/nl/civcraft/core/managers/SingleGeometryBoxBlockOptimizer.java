package nl.civcraft.core.managers;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;
import nl.civcraft.core.model.Voxel;

import java.util.List;


public class SingleGeometryBoxBlockOptimizer implements BlockOptimizer {
    @Override
    public boolean canMerge(Voxel voxel, Voxel other) {
        if (voxel == null || other == null) {
            return false;
        }
        if (equals(voxel)) {
            return false;
        } else return other.getType().equals(voxel.getType());
    }

    @Override
    public Spatial optimize(List<Voxel> voxels) {
        Node optimizedVoxelNode = new Node();
        for (Voxel voxel : voxels) {
            Geometry geometry = (Geometry) voxel.getBlock().getChild("box").clone();
            Vector3f localTranslation = geometry.getLocalTranslation();
            geometry.setLocalTranslation(localTranslation.x + voxel.getX(), localTranslation.y + voxel.getY(),localTranslation.z +  voxel.getZ());
            optimizedVoxelNode.attachChild(geometry);
        }
        Spatial optimized = GeometryBatchFactory.optimize(optimizedVoxelNode);
        optimized.setMaterial(((Geometry) voxels.get(0).getBlock().getChild("box")).getMaterial());
        return optimized;
    }
}
