package io.github.some_example_name.model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuildingType;

@Getter
@Setter
public class Lake extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.Lake;

    public Lake(Lake lake) {
        super(lake);
    }

    public Lake() {
        super.setWidth(-1);
        super.setLength(-1);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Lake(this);
    }
}
