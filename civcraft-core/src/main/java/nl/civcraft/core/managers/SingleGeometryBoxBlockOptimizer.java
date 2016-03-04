package nl.civcraft.core.managers;

import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import net.wcomohundro.jme3.csg.CSGGeometry;
import net.wcomohundro.jme3.csg.CSGShape;
import nl.civcraft.core.model.Voxel;

import java.util.List;


public class SingleGeometryBoxBlockOptimizer implements BlockOptimizer {
    @Override
    public boolean canMerge(Voxel voxel, Voxel other) {
        return !(voxel == null || other == null) && !other.equals(voxel) && other.getType().equals(voxel.getType());
    }

    @Override
    public Spatial optimize(List<Voxel> voxels) {
        CSGGeometry aGeometry = new CSGGeometry();
        aGeometry.setMaterial(((Geometry) voxels.get(0).cloneBlock().getChild("box")).getMaterial());
        for (Voxel voxel : voxels) {
            List<Geometry> geometries = voxel.cloneBlock().descendantMatches(Geometry.class);
            for (Geometry geometry : geometries) {
                geometry.setLocalTranslation(geometry.getLocalTranslation().add(voxel.getX(), voxel.getY(), voxel.getZ()));
                aGeometry.addShape(new CSGShape("voxel", geometry));
            }
        }
        aGeometry.regenerate();
        return aGeometry;
    }
}
