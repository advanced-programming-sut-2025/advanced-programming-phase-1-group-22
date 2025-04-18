package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.shelter.FarmBuilding;
import model.structure.Structure;

@Getter
@Setter
public class Cottage extends HardCodeFarmElements {
    @Override
    public void setLength(Integer length) {
        super.setLength(6);
    }

    public Cottage(HardCodeFarmElements hardCodeFarmElements) {
        super(hardCodeFarmElements);
    }

    public Cottage() {
    }

    @Override
    public void setWidth(Integer width) {
        super.setWidth(6);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Cottage(this);
    }
}
