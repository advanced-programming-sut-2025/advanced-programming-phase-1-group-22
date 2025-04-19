package model.structure;

import lombok.Getter;

@Getter
public class Stone extends Structure{
    private StoneType stoneType;
    public Stone(StoneType stoneType) {
        this.stoneType = stoneType;
    }
}
