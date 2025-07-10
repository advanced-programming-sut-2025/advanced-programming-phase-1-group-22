package io.github.some_example_name.model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuildingType;

@Getter
@Setter
public class Quarry extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Quarry;

    public Quarry(Quarry quarry) {
        super(quarry);
    }

    public Quarry() {
        super.setWidth(10);
        super.setHeight(10);
    }

    @Override
    public HardCodeFarmElements copyEl() {
        return new Quarry(this);
    }
}
