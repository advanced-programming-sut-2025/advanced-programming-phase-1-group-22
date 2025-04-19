package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lake extends HardCodeFarmElements {
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
