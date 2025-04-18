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
}
    @Override
    public void setLength(Integer length) {
        super.setLength(-1);
    }

    @Override
    public void setWidth(Integer width) {
        super.setWidth(-1);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Lake(this);
    }
}
