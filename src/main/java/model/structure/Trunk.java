package model.structure;

import lombok.Getter;

@Getter
public class Trunk extends Structure {
    private final TrunkType trunkType;
    private Boolean isBurn = false;

    public Trunk(TrunkType trunkType) {
        this.trunkType = trunkType;
    }

    public void burn() {
        this.isBurn = true;
    }
}