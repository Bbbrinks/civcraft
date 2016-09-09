package nl.civcraft.core.gamecomponents;

import nl.civcraft.core.model.Item;
import nl.civcraft.core.npc.Civvy;

import java.util.Optional;

/**
 * Created by Bob on 25-3-2016.
 * <p>
 * This is probably not worth documenting
 */
public interface Harvestable {
    Optional<Item> harvest(Civvy civvy);
}
