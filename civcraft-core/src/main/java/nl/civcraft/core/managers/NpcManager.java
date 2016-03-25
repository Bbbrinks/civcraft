package nl.civcraft.core.managers;

import nl.civcraft.core.npc.Npc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NpcManager {

    private final List<Npc> npcs;

    @Autowired
    public NpcManager(List<Npc> npcs) {
        this.npcs = npcs;
    }

    public Npc getNpc(String npcName) {
        return npcs.stream().filter(g -> g.getName().equals(npcName)).limit(1).collect(Collectors.toList()).get(0);
    }
}
