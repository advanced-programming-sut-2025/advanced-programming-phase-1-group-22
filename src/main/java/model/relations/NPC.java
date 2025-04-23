package model.relations;

import lombok.Getter;
import model.Actor;
@Getter
public class NPC extends Actor {
    private NPCType type;
    public NPC(NPCType type) {

        this.type = type;
    }
}
