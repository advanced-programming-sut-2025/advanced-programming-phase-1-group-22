package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.cook.Food;
import model.cook.FoodType;
import model.exception.InvalidInputException;
import model.gameSundry.Sundry;
import model.gameSundry.SundryType;
import model.products.TreesAndFruitsAndSeeds.MadeProduct;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.receipe.Recipe;
import model.records.Response;
import model.source.Seed;
import model.source.SeedType;
import model.tools.Tool;
import utils.App;
import model.relations.Player;


@Getter
public enum TheStardropSaloonStuff implements Shop {
	BEER(MadeProductType.BEER,400,-1),
	SALAD(FoodType.SALAD,220,-1),
	BREAD(FoodType.BREAD,120,-1),
	SPAGHETTI(FoodType.SPAGHETTI,240,-1),
	PIZZA(FoodType.PIZZA,600,-1),
	COFFEE(MadeProductType.COFFE,300,-1),
	HASHBROWNS_RECIPE(CookingRecipe.HASHBROWNS_RECIPE,50,1),
	OMELET_RECIPE(CookingRecipe.OMELET_RECIPE,100,1),
	PANCAKES_RECIPE(CookingRecipe.PANCAKES_RECIPE,100,1),
	BREAD_RECIPE(CookingRecipe.BREAD_RECIPE, 100,1),
	TORTILLA_RECIPE(CookingRecipe.TORTILLA_RECIPE,100,1),
	PIZZA_RECIPE(CookingRecipe.PIZZA_RECIPE,150,1),
	MAKI_ROLL_RECIPE(CookingRecipe.MAKI_ROLL_RECIPE,300,1),
	TRIPLE_SHOT_ESPRESSO_RECIPE(CookingRecipe.TRIPLE_SHOT_ESPRESSO_RECIPE,5000,1),
	COOKIE_RECIPE(CookingRecipe.COOKIE_RECIPE,300,1);

	private final Salable product;
	private final Integer price;
	private final Integer dailyLimit;
	private Integer dailySold = 0;

	public static String showAllProducts() {
		StringBuilder res = new StringBuilder();
		for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
			res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
		}
		return res.toString();
	}
	public static String showAvailableProducts() {
		StringBuilder res = new StringBuilder();
		for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
			if (value.dailyLimit != value.dailySold) {
				res.append(value.toString()).append(" ").append(value.getPrice()).append("$\n");
			}
		}
		return res.toString();
	}

	TheStardropSaloonStuff(Salable product, Integer price, Integer dailyLimit) {
		this.product = product;
		this.price = price;
		this.dailyLimit = dailyLimit;
	}
	public static Response purchase(String name, Integer count) {
		TheStardropSaloonStuff salable = null;
		for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
			if (value.getProduct() instanceof Recipe) continue;
			if (value.getProduct().getName().equals(name)) {
				salable = value;
			}
		}
		if (salable != null) {
			if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
				return new Response("Not enough in stock");
			}
			Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
			if (!player.getInventory().isInventoryHaveCapacity(salable.getProduct())) {
				return new Response("Not enough space in your backpack.");
			}
			if (player.getAccount().getGolds() < salable.getPrice()) {
				return new Response("Not enough golds");
			}
			player.getAccount().removeGolds(salable.getPrice());
			salable.dailySold += count;
			Salable item = null;
			if (salable.getProduct() instanceof MadeProductType) item = new MadeProduct((MadeProductType) salable.product);
			if (salable.getProduct() instanceof FoodType) item = new Food((FoodType) salable.product);
			if (item == null) throw new InvalidInputException("Item not found in Star drop Saloon");

			player.getInventory().addProductToBackPack(item, count);
			return new Response("Bought successfully", true);
		}
		for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
			if (!(value.getProduct() instanceof Recipe)) continue;
			if(value.getProduct().getName().equals(name)) {
				salable = value;
			}
		}
		if (salable == null) return new Response("Item not found");
		if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
			return new Response("Not enough in stock");
		}
		Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
		if (player.getAccount().getGolds() < salable.getPrice()) {
			return new Response("Not enough golds");
		}
		player.getAccount().removeGolds(salable.getPrice());
		salable.dailySold += count;
		player.getCookingRecipes().put((CookingRecipe) salable.product, true);
		return new Response("Bought successfully", true);
	}

	public void resetDailySold() {
		dailySold = 0;
	}
}
