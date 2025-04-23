package model.relations;

import model.Actor;

public class NPC extends Actor {
    private NPCType type;
    public NPC(NPCType type) {
        this.type = type;
    }
}
