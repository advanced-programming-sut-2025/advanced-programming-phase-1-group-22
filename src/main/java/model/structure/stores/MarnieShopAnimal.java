package model.structure.stores;

import lombok.Getter;
import model.animal.AnimalType;
import model.shelter.FarmBuildingType;
@Getter
public enum MarnieShopAnimal {
	CHICKEN(AnimalType.HEN,800, FarmBuildingType.COOP,2),
	COW(AnimalType.COW,1500, FarmBuildingType.BARN,2),
	GOAT(AnimalType.GOAT,4000, FarmBuildingType.BIG_BARN,2),
	DUCK(AnimalType.DUCK,1200, FarmBuildingType.BIG_COOP,2),
	SHEEP(AnimalType.SHEEP,8000, FarmBuildingType.DELUXE_BARN,2),
	RABBIT(AnimalType.RABBIT,8000, FarmBuildingType.DELUXE_COOP,2),
	DINOSAUR(AnimalType.DINOSAUR,14000, FarmBuildingType.BIG_COOP,2),
	PIG(AnimalType.PIG,16000, FarmBuildingType.DELUXE_BARN,2);

	private final AnimalType animalType;
	private final Integer price;
	private final FarmBuildingType buildingRequired;
	private final Integer dailyLimit;

	MarnieShopAnimal(AnimalType animalType, Integer price, FarmBuildingType buildingRequired, Integer dailyLimit) {
		this.animalType = animalType;
		this.price = price;
		this.buildingRequired = buildingRequired;
		this.dailyLimit = dailyLimit;
	}
}
