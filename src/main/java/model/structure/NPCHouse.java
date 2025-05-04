package model.structure;

import lombok.Getter;
import lombok.Setter;
import model.relations.NPC;
import model.shelter.FarmBuilding;

@Getter
@Setter
public class NPCHouse extends Structure {
    private NPC owner;

    public NPCHouse(NPC owner) {
        this.owner = owner;
    }
}
