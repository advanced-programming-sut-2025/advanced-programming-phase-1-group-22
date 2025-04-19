package model.structure.stores;

import lombok.Getter;
import model.cook.FoodType;
import model.receipe.Recipe;
import model.tools.FishingPole;
@Getter
public enum FishShopStuff {
	FISH_SMOKER_RECIPE(new Recipe("fish smoker recipe","A recipe to make Fish Smoker",10000),10000,0,1),
	BAMBOO_POLE(FishingPole.BAMBOO,500,0,1),
	TRAINING_ROD(FishingPole.TRAINING,25,0,1),
	FIBERGLASS_ROD(FishingPole.FIBER_GLASS,1800,2,1),
	IRIDIUM_ROD(FishingPole.IRIDIUM,7500,4,1);

	private Recipe recipe;
	private FoodType foodType;
	private FishingPole fishingPole;
	private final Integer price;
	private final Integer fishingSkillRequired;
	private final Integer dailyLimit;

	FishShopStuff(Recipe recipe, Integer price, Integer fishingSkillRequired, Integer dailyLimit) {
		this.recipe = recipe;
		this.price = price;
		this.fishingSkillRequired = fishingSkillRequired;
		this.dailyLimit = dailyLimit;
	}

	FishShopStuff(FishingPole fishingPole, Integer price, Integer fishingSkillRequired, Integer dailyLimit) {
		this.fishingPole = fishingPole;
		this.price = price;
		this.fishingSkillRequired = fishingSkillRequired;
		this.dailyLimit = dailyLimit;
	}
}
