package model.structure.stores;

import lombok.Getter;
import model.animal.AnimalType;
import model.shelter.FarmBuildingType;
@Getter
public enum MarnieShopAnimal {
	CHICKEN("chicken",AnimalType.HEN,800, FarmBuildingType.COOP,2),
	COW("cow",AnimalType.COW,1500, FarmBuildingType.BARN,2),
	GOAT("goat",AnimalType.GOAT,4000, FarmBuildingType.BIG_BARN,2),
	DUCK("duck",AnimalType.DUCK,1200, FarmBuildingType.BIG_COOP,2),
	SHEEP("sheep",AnimalType.SHEEP,8000, FarmBuildingType.DELUXE_BARN,2),
	RABBIT("rabbit",AnimalType.RABBIT,8000, FarmBuildingType.DELUXE_COOP,2),
	DINOSAUR("dinosaur",AnimalType.DINOSAUR,14000, FarmBuildingType.BIG_COOP,2),
	PIG("pig",AnimalType.PIG,16000, FarmBuildingType.DELUXE_BARN,2);

	private final String name;
	private final AnimalType animalType;
	private final Integer price;
	private final FarmBuildingType buildingRequired;
	private final Integer dailyLimit;

	MarnieShopAnimal(String name,AnimalType animalType, Integer price, FarmBuildingType buildingRequired, Integer dailyLimit) {
		this.name = name;
		this.animalType = animalType;
		this.price = price;
		this.buildingRequired = buildingRequired;
		this.dailyLimit = dailyLimit;
	}

	public static MarnieShopAnimal getFromName(String name){
		for (MarnieShopAnimal value : MarnieShopAnimal.values()) {
			if (value.name.equals(name.toLowerCase())){
				return value;
			}
		}
		return null;
	}
}
