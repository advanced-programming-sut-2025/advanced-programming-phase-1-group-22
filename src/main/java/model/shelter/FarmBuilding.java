package model.shelter;

import lombok.Getter;
import lombok.Setter;
import model.animal.Animal;
import model.structure.Structure;

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
