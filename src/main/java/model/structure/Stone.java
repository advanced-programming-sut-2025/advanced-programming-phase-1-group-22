package model.structure;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Stone extends Structure{
    private StoneType stoneType;
    public Stone(StoneType stoneType) {
        this.stoneType = stoneType;
    }
}
