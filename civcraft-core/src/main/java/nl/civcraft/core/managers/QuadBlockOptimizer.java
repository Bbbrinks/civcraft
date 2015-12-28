package nl.civcraft.core.managers;


import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;
import nl.civcraft.core.model.Voxel;

import java.util.ArrayList;
import java.util.List;

public class QuadBlockOptimizer implements BlockOptimizer {
    @Override
    public boolean canMerge(Voxel voxel, Voxel other) {
        return !(voxel == null || other == null) && !other.equals(voxel) && other.getType().equals(voxel.getType());
    }

    @Override
    public Spatial optimize(List<Voxel> voxels) {
        List<Geometry> allGeometries = new ArrayList<>();
        for (Voxel voxel : voxels) {
            List<Geometry> geometries = voxel.cloneBlock().descendantMatches(Geometry.class);
            for (Geometry geometry : geometries) {
                geometry.setLocalTranslation(geometry.getLocalTranslation().add(voxel.getX(), voxel.getY(), voxel.getZ()));
                allGeometries.add(geometry);
            }
        }
        List<Geometry> batches = GeometryBatchFactory.makeBatches(allGeometries);
        Node optimizedVoxels = new Node();
        for (Geometry batch : batches) {
            optimizedVoxels.attachChild(batch);
        }
        return optimizedVoxels;
    }
}
