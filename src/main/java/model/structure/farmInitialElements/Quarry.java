package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.shelter.FarmBuildingType;

@Getter
@Setter
public class Quarry extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Quarry;

    public Quarry(Quarry quarry) {
        super(quarry);
    }

    public Quarry() {
        super.setLength(10);
        super.setWidth(10);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Quarry(this);
    }
}
