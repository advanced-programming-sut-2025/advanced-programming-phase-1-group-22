package io.github.some_example_name.model.structure.farmInitialElements;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.shelter.FarmBuilding;
import io.github.some_example_name.model.shelter.FarmBuildingType;
import io.github.some_example_name.model.structure.Structure;

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
