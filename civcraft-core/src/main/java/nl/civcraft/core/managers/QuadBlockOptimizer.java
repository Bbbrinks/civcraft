package nl.civcraft.core.managers;


import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.optimize.GeometryBatchFactory;
import nl.civcraft.core.model.Face;
import nl.civcraft.core.model.Voxel;

import java.util.List;

public class QuadBlockOptimizer implements BlockOptimizer {
    @Override
    public boolean canMerge(Voxel voxel, Voxel other) {
        return !(voxel == null || other == null) && !other.equals(voxel) && other.getType().equals(voxel.getType());
    }

    @Override
    public Spatial optimize(List<Voxel> voxels) {
        Node combined = new Node();
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.TOP));
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.BOTTOM));
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.LEFT));
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.RIGHT));
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.FRONT));
        combined.attachChild(getOptimizedSpatialByFace(voxels, Face.BACK));
        return combined;
    }

    private Spatial getOptimizedSpatialByFace(List<Voxel> voxels, Face face) {
        String faceStr;
        switch (face) {
            case TOP:
                faceStr = "top";
                break;
            case BOTTOM:
                faceStr = "bottom";
                break;
            case LEFT:
                faceStr = "left";
                break;
            case RIGHT:
                faceStr = "right";
                break;
            case FRONT:
                faceStr = "front";
                break;
            case BACK:
                faceStr = "back";
                break;
            default:
                faceStr = "none";
                break;
        }
        Node allFaces = new Node();

        for (Voxel voxel : voxels) {
            Geometry geometry = (Geometry) voxel.getBlock().getChild(faceStr).clone();
            geometry.setLocalTranslation(geometry.getLocalTranslation().add(voxel.getX(), voxel.getY(), voxel.getZ()));
            allFaces.attachChild(geometry);
        }
        Spatial spatial = GeometryBatchFactory.optimize(allFaces);
        spatial.setMaterial(((Geometry) voxels.get(0).getBlock().getChild(faceStr)).getMaterial());
        return spatial;
    }
}
