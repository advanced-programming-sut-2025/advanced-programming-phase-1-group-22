package model.shelter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.structure.farmInitialElements.HardCodeFarmElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class ShippingBin extends HardCodeFarmElements {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.SHIPPING_BIN;
    private Map<Salable,Integer> salable = new HashMap<>();

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

    public Integer CalculatePriceOfShippingBinProducts(){
        int price = 0;

        for (Map.Entry<Salable, Integer> salableIntegerEntry : salable.entrySet()) {
            price += salableIntegerEntry.getKey().getSellPrice() * salableIntegerEntry.getValue();
        }

        salable.clear();
        return price;
    }
}