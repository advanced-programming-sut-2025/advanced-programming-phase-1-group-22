package model.structure;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Trunk extends Structure {
    private  TrunkType trunkType;
    private Boolean isBurn = false;

    public Trunk(TrunkType trunkType) {
        this.trunkType = trunkType;
    }

    public void burn() {
        this.isBurn = true;
    }
}