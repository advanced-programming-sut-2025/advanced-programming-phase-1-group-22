package model.shelter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.structure.farmInitialElements.HardCodeFarmElements;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class ShippingBin extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.SHIPPING_BIN;
    private List<Salable> salable = new ArrayList<>();

    public ShippingBin(ShippingBin shippingBin) {
        super(shippingBin);
    }

    public ShippingBin() {
        super.setWidth(1);
        super.setLength(1);
    }

    @Override
    public HardCodeFarmElements cloneEl() {
        return new ShippingBin(this);
    }
}