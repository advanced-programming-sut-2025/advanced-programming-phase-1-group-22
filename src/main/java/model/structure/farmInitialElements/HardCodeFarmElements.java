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

    public HardCodeFarmElements() {
    }

    public HardCodeFarmElements(HardCodeFarmElements hardCodeFarmElements) {
        this.tilePairList = hardCodeFarmElements.tilePairList;
        this.length = hardCodeFarmElements.length;
        this.width = hardCodeFarmElements.width;
    }

    public abstract HardCodeFarmElements cloneEl();
}
