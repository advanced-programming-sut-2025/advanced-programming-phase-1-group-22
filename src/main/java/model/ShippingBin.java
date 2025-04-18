package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.shelter.FarmBuildingType;
import model.structure.Structure;

import java.util.List;

@Getter
@Setter
@ToString
public class ShippingBin extends   Structure {
    private final FarmBuildingType farmBuildingType = FarmBuildingType.SHIPPING_BIN;
    private List<Salable> salables;
}