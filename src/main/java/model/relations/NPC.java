package model.relations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import model.Actor;
@Getter
@NoArgsConstructor
public class NPC extends Actor {
    private NPCType type;
    public NPC(NPCType type) {

        this.type = type;
    }
}
