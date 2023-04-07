package nl.civcraft.core.gamecomponents;


import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bob on 9-6-2017.
 * <p>
 * This is probably not worth documenting
 */
public class PlanningGhost extends AbstractGameComponent {

    private final List<Matrix4f> plannedVoxels;

    public PlanningGhost() {
        plannedVoxels = new ArrayList<>();
    }

    public List<Matrix4f> getPlannedVoxels() {
        return plannedVoxels;
    }

    public void setPlannedVoxels(List<Matrix4f> plannedVoxels) {
        this.plannedVoxels.clear();
        this.plannedVoxels.addAll(plannedVoxels);
        gameObject.changed();
    }

    public static class Factory implements GameComponentFactory<PlanningGhost> {

        @Override
        public PlanningGhost build() {
            return new PlanningGhost();
        }

        @Override
        public Class<PlanningGhost> getComponentType() {
            return PlanningGhost.class;
        }
    }
}
