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

    }
    @Override
    public void setLength(Integer length) {
        super.setLength(12);
    }

    @Override
    public void setWidth(Integer width) {
        super.setWidth(10);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new GreenHouse(this);
    }
}
