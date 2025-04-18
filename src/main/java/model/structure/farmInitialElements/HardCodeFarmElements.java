package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.Pair;
import model.structure.Structure;

import java.util.List;

@Getter
@Setter
public abstract class HardCodeFarmElements extends Structure {
    List<Pair> tilePairList;
    private Integer length;
    private Integer width;
}
