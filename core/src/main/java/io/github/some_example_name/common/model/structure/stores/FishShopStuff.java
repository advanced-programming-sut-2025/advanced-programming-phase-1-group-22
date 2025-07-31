package io.github.some_example_name.common.model.structure.stores;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.source.Mineral;
import lombok.Getter;
import io.github.some_example_name.common.model.abilitiy.Ability;
import io.github.some_example_name.common.model.cook.Food;
import io.github.some_example_name.common.model.cook.FoodType;
import io.github.some_example_name.common.model.receipe.CraftingRecipe;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.model.relations.Player;
import io.github.some_example_name.common.model.tools.FishingPole;
import io.github.some_example_name.common.utils.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public enum FishShopStuff  implements Shop{
	FISH_SMOKER_RECIPE(CraftingRecipe.FISH_SMOKER_RECIPE,10000,0,1),
	TROUT_SOUP(FoodType.TROUT_SOUP,250,0,1),
	BAMBOO_POLE(FishingPole.BAMBOO,500,0,1),
	TRAINING_ROD(FishingPole.TRAINING,25,0,1),
	FIBERGLASS_ROD(FishingPole.FIBER_GLASS,1800,2,1) {
		@Override
		public boolean isAvailable() {
			if (App.getInstance().getCurrentGame().getCurrentPlayer().getAbilityLevel(Ability.FISHING) < 2) return false;
			return super.isAvailable();
		}
	},
	IRIDIUM_ROD(FishingPole.IRIDIUM,7500,4,1) {
		@Override
		public boolean isAvailable() {
			if (App.getInstance().getCurrentGame().getCurrentPlayer().getAbilityLevel(Ability.FISHING) < 4) return false;
			return super.isAvailable();
		}
	};

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

	FishShopStuff(FoodType foodType, Integer price, Integer fishingSkillRequired, Integer dailyLimit) {
		this.foodType = foodType;
		this.price = price;
		this.fishingSkillRequired = fishingSkillRequired;
		this.dailyLimit = dailyLimit;
	}

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        for (FishShopStuff value : FishShopStuff.values()) {
            boolean available = value.isAvailable();
            if (value.craftingRecipe != null) items.add(new Item(value.craftingRecipe,value.price, value.dailyLimit,value.dailySold,available,null,value));
            else if (value.fishingPole != null) items.add(new Item(value.fishingPole,value.price, value.dailyLimit,value.dailySold,available,null,value));
            else if (value.foodType != null) items.add(new Item(value.foodType,value.price, value.dailyLimit,value.dailySold,available,null,value));
        }
        return items;
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
			if (value.isAvailable()) {
				res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
			}
		}
		return res.toString();
	}
	public static Response purchase(String name, Integer count) {
		FishShopStuff salable = null;
		if (name.equalsIgnoreCase("fish smoker recipe")) {
			salable = FishShopStuff.FISH_SMOKER_RECIPE;
			if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
				return new Response("Not enough in stock");
			}
			Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
//			if (!player.getInventory().isInventoryHaveCapacity(salable.craftingRecipe)) {
//				return new Response("Not enough space in your backpack.");
//			}
			if (player.getAccount().getGolds() < salable.getPrice()) {
				return new Response("Not enough golds");
			}
			player.getAccount().removeGolds(salable.getPrice());
            GameClient.getInstance().updatePlayerGold(player);
			salable.dailySold += count;
			player.getCraftingRecipes().put(CraftingRecipe.FISH_SMOKER_RECIPE, true);
			return new Response("Bought successfully", true);
		}
		if (name.equalsIgnoreCase("trout soup")) {
			salable = FishShopStuff.TROUT_SOUP;
			if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
				return new Response("Not enough in stock");
			}
			Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
			if (!player.getInventory().isInventoryHaveCapacity(salable.foodType)) {
				return new Response("Not enough space in your backpack.");
			}
			if (player.getAccount().getGolds() < salable.getPrice()) {
				return new Response("Not enough golds");
			}
			player.getAccount().removeGolds(salable.getPrice());
            GameClient.getInstance().updatePlayerGold(player);
			salable.dailySold += count;
			player.getInventory().addProductToBackPack(new Food(salable.foodType), count);
            GameClient.getInstance().updatePlayerAddToInventory(player,new Food(salable.foodType),count);
            return new Response("Bought successfully", true);
		}
		for (FishShopStuff value : FishShopStuff.values()) {
			if (value.ordinal() < 2) continue;
			if(value.getFishingPole().getName().equalsIgnoreCase(name)) {
				salable = value;
			}
		}
		if (salable == null) return new Response("Item not found");
		if (!salable.isAvailable()) return new Response("This item isn't available now, come back later.");
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
        GameClient.getInstance().updatePlayerGold(player);
		salable.dailySold += count;
		player.getInventory().addProductToBackPack(salable.fishingPole, count);
        GameClient.getInstance().updatePlayerAddToInventory(player, salable.fishingPole, count);
        return new Response("Bought successfully", true);
	}

	public boolean isAvailable() {
		return !Objects.equals(this.dailyLimit, this.dailySold);
	}

	public void resetDailySold() {
		dailySold = 0;
	}

    public void increaseDailySold(int amount){
        dailySold += amount;
    }
}
