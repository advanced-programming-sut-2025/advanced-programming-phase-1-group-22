package model.structure.stores;

import lombok.Getter;
import model.Salable;
import model.cook.FoodType;
import model.products.Product;
import model.products.TreesAndFruitsAndSeeds.MadeProductType;
import model.receipe.Recipe;
@Getter
public enum TheStardropSaloonStuff {
	BEER(MadeProductType.BEER,400,-1),
	SALAD(FoodType.SALAD,220,-1),
	BREAD(FoodType.BREAD,120,-1),
	SPAGHETTI(FoodType.SPAGHETTI,240,-1),
	PIZZA(FoodType.PIZZA,600,-1),
	COFFEE(MadeProductType.COFFE,300,-1),
	HASHBROWNS_RECIPE(new Recipe("hashbrowns recipe","A recipe to make Hashbrowns",50),50,1),
	OMELET_RECIPE(new Recipe("omelet recipe","A recipe to make Omelet",100),100,1),
	PANCAKES_RECIPE(new Recipe("pancakes recipe","A recipe to make Pancakes",100),100,1),
	BREAD_RECIPE(new Recipe("bread recipe","A recipe to make Bread",100),100,1),
	TORTILLA_RECIPE(new Recipe("tortilla recipe","A recipe to make Tortilla",100),100,1),
	PIZZA_RECIPE(new Recipe("pizza recipe","A recipe to make Pizza",150),150,1),
	MAKI_ROLL_RECIPE(new Recipe("maki roll recipe","A recipe to make Maki Roll",300),300,1),
	TRIPLE_SHOT_ESPRESSO_RECIPE(new Recipe("triple shot espresso recipe","A recipe to make Triple Shot Espresso",5000),5000,1),
	COOKIE_RECIPE(new Recipe("cookie recipe","A recipe to make Cookie",300),300,1);

	private final Salable product;
	private final Integer price;
	private final Integer dailyLimit;

	TheStardropSaloonStuff(Salable product, Integer price, Integer dailyLimit) {
		this.product = product;
		this.price = price;
		this.dailyLimit = dailyLimit;
	}
}
