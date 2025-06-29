package io.github.some_example_name.model.shelter;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.animal.Animal;
import io.github.some_example_name.model.structure.Structure;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FarmBuilding extends Structure {
	private FarmBuildingType farmBuildingType;
	private List<Animal> animals = new ArrayList<>();

	public FarmBuilding(FarmBuildingType farmBuildingType) {
		this.farmBuildingType = farmBuildingType;
	}

	public Boolean canAddNewAnimal(){
		return farmBuildingType.getCapacity() >= animals.size() + 1;
	}
}
