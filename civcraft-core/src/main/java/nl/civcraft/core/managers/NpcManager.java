package nl.civcraft.core.managers;

import nl.civcraft.core.npc.Npc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Bob on 29-12-2015.
 * <p>
 * This is probably not worth documenting
 */
public class NpcManager {
    @Autowired
    private List<Npc> npcs;

    public Npc getNpc(String npcName) {
        return npcs.stream().filter(g -> g.getName().equals(npcName)).limit(1).collect(Collectors.toList()).get(0);
    }
}
