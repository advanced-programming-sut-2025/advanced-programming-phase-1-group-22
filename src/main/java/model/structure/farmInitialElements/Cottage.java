package model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import model.shelter.FarmBuilding;
import model.shelter.FarmBuildingType;
import model.structure.Structure;

@Getter
@Setter
public class Cottage extends HardCodeFarmElements {
	private final FarmBuildingType farmBuildingType = FarmBuildingType.Cottage;

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
