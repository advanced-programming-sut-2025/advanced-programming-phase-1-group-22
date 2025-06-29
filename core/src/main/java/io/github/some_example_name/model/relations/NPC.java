package io.github.some_example_name.model.relations;

import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.some_example_name.model.Actor;
@Getter
@NoArgsConstructor
public class NPC extends Actor {
    private NPCType type;
    public NPC(NPCType type) {

        this.type = type;
    }
}
