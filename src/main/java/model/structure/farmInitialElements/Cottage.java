package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.shelter.FarmBuilding;
import model.structure.Structure;

@Getter
@Setter
public class Cottage extends HardCodeFarmElements {

    public Cottage(HardCodeFarmElements hardCodeFarmElements) {
        super(hardCodeFarmElements);
    }

    public Cottage() {
        super.setLength(6);
        super.setWidth(6);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Cottage(this);
    }
}
