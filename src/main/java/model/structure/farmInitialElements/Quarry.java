package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quarry extends HardCodeFarmElements {
    public Quarry(Quarry quarry) {
        super(quarry);
    }
public Quarry() {

}
    @Override
    public void setLength(Integer length) {
        super.setLength(12);
    }

    @Override
    public void setWidth(Integer width) {
        super.setWidth(12);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new Quarry(this);
    }
}
