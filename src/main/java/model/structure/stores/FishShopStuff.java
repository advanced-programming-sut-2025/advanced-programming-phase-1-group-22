package model.structure.stores;

import lombok.Getter;
import model.cook.FoodType;
import model.craft.CraftType;
import model.receipe.CraftingRecipe;
import model.records.Response;
import model.relations.Player;
import model.tools.FishingPole;
import utils.App;

@Getter
public enum FishShopStuff  implements Shop{
	FISH_SMOKER_RECIPE(new CraftingRecipe("fish smoker recipe","A recipe to make Fish Smoker",10000, CraftType.FISH_SMOKER),10000,0,1),
	BAMBOO_POLE(FishingPole.BAMBOO,500,0,1),
	TRAINING_ROD(FishingPole.TRAINING,25,0,1),
	FIBERGLASS_ROD(FishingPole.FIBER_GLASS,1800,2,1),
	IRIDIUM_ROD(FishingPole.IRIDIUM,7500,4,1);

	private CraftingRecipe craftingRecipe;
	private FoodType foodType;
	private FishingPole fishingPole;
	private final Integer price;
	private final Integer fishingSkillRequired;
	private final Integer dailyLimit;
	private Integer dailySold = 0;

	FishShopStuff(CraftingRecipe craftingRecipe, Integer price, Integer fishingSkillRequired, Integer dailyLimit) {
		this.craftingRecipe = craftingRecipe;
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
	public static String showAllProducts() {
		StringBuilder res = new StringBuilder();
		for (FishShopStuff value : FishShopStuff.values()) {
			res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
		}
		return res.toString();
	}
	public static String showAvailableProducts() {
		StringBuilder res = new StringBuilder();
		for (FishShopStuff value : FishShopStuff.values()) {
			if (value.dailyLimit != value.dailySold) {
				res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
			}
		}
		return res.toString();
	}
	public static Response purchase(String name, Integer count) {
		FishShopStuff salable = null;
		if (name.equals("fish smoker recipe")) {
			salable = FishShopStuff.FISH_SMOKER_RECIPE;
			if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
				return new Response("Not enough in stock");
			}
			Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
			if (!player.getInventory().isInventoryHaveCapacity(salable.fishingPole)) {
				return new Response("Not enough space in your backpack.");
			}
			if (player.getAccount().getGolds() < salable.getPrice()) {
				return new Response("Not enough golds");
			}
			player.getAccount().removeGolds(salable.getPrice());
			salable.dailySold += count;
			player.getInventory().addProductToBackPack(salable.fishingPole, count);
			return new Response("Bought successfully", true);
		}
		for (FishShopStuff value : FishShopStuff.values()) {
			if(value.getFishingPole().getName().equals(name)) {
				salable = value;
			}
		}
		if (salable == null) return null;
		if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
			return new Response("Not enough in stock");
		}
		Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
		if (!player.getInventory().isInventoryHaveCapacity(salable.fishingPole)) {
			return new Response("Not enough space in your backpack.");
		}
		if (player.getAccount().getGolds() < salable.getPrice()) {
			return new Response("Not enough golds");
		}
		player.getAccount().removeGolds(salable.getPrice());
		salable.dailySold += count;
		player.getInventory().addProductToBackPack(salable.fishingPole, count);
		return new Response("Bought successfully", true);
	}
}
