package io.github.some_example_name.model.structure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.model.relations.NPC;
import io.github.some_example_name.model.shelter.FarmBuilding;

@Getter
@Setter
@NoArgsConstructor
public class NPCHouse extends Structure {
    private NPC owner;

    public NPCHouse(NPC owner) {
        this.owner = owner;
    }
}
