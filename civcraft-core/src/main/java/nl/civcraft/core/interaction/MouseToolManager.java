package nl.civcraft.core.interaction;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Bob on 3-3-2018.
 * <p>
 * This is probably not worth documenting
 */
@Singleton
public class MouseToolManager {
    private final Set<MouseTool> mouseTools;
    private final MouseTool defaultMouseTool;
    private MouseTool selectedMouseTool;

    @Inject
    public MouseToolManager(Set<MouseTool> mouseTools,
                            @Named("singleVoxelSelector") MouseTool defaultMouseTool) {
        this.mouseTools = mouseTools;
        this.defaultMouseTool = defaultMouseTool;
    }

    public MouseTool getSelectedMouseTool() {
        return Optional.ofNullable(selectedMouseTool).orElse(defaultMouseTool);
    }

    public void setSelectedMouseTool(MouseTool selectedMouseTool) {
        this.selectedMouseTool = selectedMouseTool;
    }

    public Set<MouseTool> getMouseTools() {
        return mouseTools;
    }
}
