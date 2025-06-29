package io.github.some_example_name.model.receipe;

import lombok.Getter;
import io.github.some_example_name.model.Fridge;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.cook.FoodType;
import io.github.some_example_name.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.utils.App;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Getter
public enum CookingRecipe implements Recipe {
    FRIED_EGG_RECIPE("fried egg recipe", "A recipe to make fried egg", 0, () -> FoodType.FRIED_EGG),
    BAKED_FISH_RECIPE("backed fish recipe", "A recipe to make backed fish", 0, () -> FoodType.BACKED_FISH),
    SALAD_RECIPE("salad recipe", "A recipe to make Salad", 0, () -> FoodType.SALAD),
    SALMON_DINNER_RECIPE("salmon dinner recipe", "A recipe to make Salmon Dinner", 0, () -> FoodType.SALMON_DINNER),
    HASHBROWNS_RECIPE("hashbrowns recipe", "A recipe to make Hashbrowns", 50, () -> FoodType.HASH_BROWNS),
    OMELET_RECIPE("omelet recipe", "A recipe to make Omelet", 100, () -> FoodType.OMELET),
    PANCAKES_RECIPE("pancakes recipe", "A recipe to make Pancakes", 100, () -> FoodType.PANCAKES),
    BREAD_RECIPE("bread recipe", "A recipe to make Bread", 100, () -> FoodType.BREAD),
    TORTILLA_RECIPE("tortilla recipe", "A recipe to make Tortilla", 100, () -> FoodType.TORTILLA),
    PIZZA_RECIPE("pizza recipe", "A recipe to make Pizza", 150, () -> FoodType.PIZZA),
    MAKI_ROLL_RECIPE("maki roll recipe", "A recipe to make Maki Roll", 300, () -> FoodType.MAKI_ROLL),
    TRIPLE_SHOT_ESPRESSO_RECIPE("triple shot espresso recipe", "A recipe to make Triple Shot Espresso", 5000, () -> FoodType.TRIPLE_SHOT_ESPRESSO),
    COOKIE_RECIPE("cookie recipe", "A recipe to make Cookie", 300, () -> FoodType.COOKIE),
    VEGETABLE_MEDLEY_RECIPE("vegetable medley recipe", "A recipe to make vegetable medley", 120, () -> FoodType.VEGETABLE_MEDLEY),
    FARMERS_LUNCH_RECIPE("farmer's lunch recipe", "A recipe to make farmer's lunch", 150, () -> FoodType.FORMER_LUNCH),
    SURVIVAL_BURGER_RECIPE("survival burger recipe", "A recipe to make survival burger", 180, () -> FoodType.SURVIVAL_BURGER),
    DISH_O_THE_SEA_RECIPE("dish O' the Sea recipe", "A recipe to make dish O' the Sea", 220, () -> FoodType.DISH_O_THE_SEA),
    SEAFOAM_PUDDING_RECIPE("seafoam pudding recipe", "A recipe to make seafoam pudding", 300, () -> FoodType.SEA_FORM_PUDDING),
    MINERS_TREAT_RECIPE("miner's treat recipe", "A recipe to make miner's treat", 200, () -> FoodType.MINERS_TREAT);

    private String name;
    private Integer id;
    private String description;
    private Integer price;

    @FunctionalInterface
    private interface IngredientsSupplier {
        FoodType get();
    }

    private final IngredientsSupplier ingredientsSupplier;
    private transient AtomicReference<FoodType> foodType = new AtomicReference<>();

    private CookingRecipe(String name, String description, Integer price, IngredientsSupplier foodType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredientsSupplier = foodType;
    }

    public FoodType getIngredients() {
        return foodType.updateAndGet(cache ->
                cache != null ? cache : ingredientsSupplier.get()
        );
    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public String toString() {
        Fridge fridge = App.getInstance().getCurrentGame().findFarm().getFridge();
        String res = this.getIngredients().getName() + ":\nIngredients:\n";
        res += this.getIngredients().getProductsString();
        res += "Energy: " + this.getIngredients().getEnergy();
        res += "\nPrice: " + this.getIngredients().getSellPrice() + "$\n";
        res += this.getIngredients().isValidIngredient(fridge, App.getInstance().getCurrentGame().getCurrentPlayer()) ?
                                                                "You can cook it" :
                                                                "You don't have required ingredients" + "\n";
        return res;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}
