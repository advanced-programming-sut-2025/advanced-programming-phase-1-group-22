package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.cook.FoodType;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.CookingRecipe;
import model.receipe.CraftingRecipe;
import model.receipe.Recipe;
import model.records.Response;
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
	HASHBROWNS_RECIPE(new CookingRecipe("hashbrowns recipe","A recipe to make Hashbrowns",50, FoodType.HASH_BROWNS),50,1),
	OMELET_RECIPE(new CookingRecipe("omelet recipe","A recipe to make Omelet",100, FoodType.OMELET),100,1),
	PANCAKES_RECIPE(new CookingRecipe("pancakes recipe","A recipe to make Pancakes",100, FoodType.PANCAKES),100,1),
	BREAD_RECIPE(new CookingRecipe("bread recipe","A recipe to make Bread",100, FoodType.BREAD),100,1),
	TORTILLA_RECIPE(new CookingRecipe("tortilla recipe","A recipe to make Tortilla",100, FoodType.TORTILLA),100,1),
	PIZZA_RECIPE(new CookingRecipe("pizza recipe","A recipe to make Pizza",150, FoodType.PIZZA),150,1),
	MAKI_ROLL_RECIPE(new CookingRecipe("maki roll recipe","A recipe to make Maki Roll",300, FoodType.MAKI_ROLL),300,1),
	TRIPLE_SHOT_ESPRESSO_RECIPE(new CookingRecipe("triple shot espresso recipe","A recipe to make Triple Shot Espresso",5000, FoodType.TRIPLE_SHOT_ESPRESSO),5000,1),
	COOKIE_RECIPE(new CookingRecipe("cookie recipe","A recipe to make Cookie",300, FoodType.COOKIE),300,1);

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
			player.getInventory().addProductToBackPack(salable.product, count);
			return new Response("Bought successfully", true);
		}
		for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
			if (!(value.getProduct() instanceof Recipe)) continue;
			if(value.getProduct().getName().equals(name)) {
				salable = value;
			}
		}
		if (salable == null) return null;
		if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
			return new Response("Not enough in stock");
		}
		Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
		if (player.getAccount().getGolds() < salable.getPrice()) {
			return new Response("Not enough golds");
		}
		player.getAccount().removeGolds(salable.getPrice());
		salable.dailySold += count;
		player.getCookingRecipes().add((CookingRecipe) salable.product);
		return new Response("Bought successfully", true);
	}
}
