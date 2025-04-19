package model.structure;

import lombok.Getter;

@Getter
public class Trunk extends Structure {
    private TrunkType trunkType;

    public Trunk(TrunkType trunkType) {
        this.trunkType = trunkType;
    }
}
