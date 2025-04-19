package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.shelter.FarmBuilding;
import model.structure.Structure;

@Getter
@Setter
public class GreenHouse extends HardCodeFarmElements {
    public GreenHouse(GreenHouse greenHouse) {
        super(greenHouse);
    }

    public GreenHouse() {
        super.setLength(8);
        super.setWidth(7);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new GreenHouse(this);
    }
}
