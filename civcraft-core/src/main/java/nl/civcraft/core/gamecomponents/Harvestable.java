package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.npc.Civvy;
import nl.civcraft.core.tasks.Task;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Harvestable {
    Task.Result harvest(Civvy civvy);
}
