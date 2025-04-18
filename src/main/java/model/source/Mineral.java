package model.source;

import lombok.Getter;
import lombok.Setter;
import model.structure.Structure;

@Getter
@Setter
public class Mineral extends Structure {
    private MineralType foragingMineralType;
}
