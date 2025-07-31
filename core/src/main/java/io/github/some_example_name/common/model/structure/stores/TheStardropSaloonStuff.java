package io.github.some_example_name.common.model.structure.stores;

import io.github.some_example_name.client.GameClient;
import io.github.some_example_name.common.model.source.Mineral;
import lombok.Getter;
import io.github.some_example_name.common.model.Salable;
import io.github.some_example_name.common.model.cook.Food;
import io.github.some_example_name.common.model.cook.FoodType;
import io.github.some_example_name.common.model.exception.InvalidInputException;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProduct;
import io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds.MadeProductType;
import io.github.some_example_name.common.model.receipe.CookingRecipe;
import io.github.some_example_name.common.model.receipe.Recipe;
import io.github.some_example_name.common.model.records.Response;
import io.github.some_example_name.common.utils.App;
import io.github.some_example_name.common.model.relations.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Getter
public enum TheStardropSaloonStuff implements Shop {
    BEER(() -> MadeProductType.BEER, 400, -1),
    SALAD(() -> FoodType.SALAD, 220, -1),
    BREAD(() -> FoodType.BREAD, 120, -1),
    SPAGHETTI(() -> FoodType.SPAGHETTI, 240, -1),
    PIZZA(() -> FoodType.PIZZA, 600, -1),
    COFFEE(() -> MadeProductType.COFFE, 300, -1),
    HASHBROWNS_RECIPE(() -> CookingRecipe.HASHBROWNS_RECIPE, 50, 1),
    OMELET_RECIPE(() -> CookingRecipe.OMELET_RECIPE, 100, 1),
    PANCAKES_RECIPE(() -> CookingRecipe.PANCAKES_RECIPE, 100, 1),
    BREAD_RECIPE(() -> CookingRecipe.BREAD_RECIPE, 100, 1),
    TORTILLA_RECIPE(() -> CookingRecipe.TORTILLA_RECIPE, 100, 1),
    PIZZA_RECIPE(() -> CookingRecipe.PIZZA_RECIPE, 150, 1),
    MAKI_ROLL_RECIPE(() -> CookingRecipe.MAKI_ROLL_RECIPE, 300, 1),
    TRIPLE_SHOT_ESPRESSO_RECIPE(() -> CookingRecipe.TRIPLE_SHOT_ESPRESSO_RECIPE, 5000, 1),
    COOKIE_RECIPE(() -> CookingRecipe.COOKIE_RECIPE, 300, 1);

    private final Integer price;
    private final Integer dailyLimit;
    private Integer dailySold = 0;
    private transient volatile Salable product;

    @FunctionalInterface
    private interface IngredientsSupplier {
        Salable get();
    }

    private final IngredientsSupplier ingredientsSupplier;

    public Salable getSalable() {
        if (product == null) {
            product = ingredientsSupplier.get();
        }
        return product;
    }

    public static List<Item> getItems(){
        List<Item> items = new ArrayList<>();
        for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
            boolean available = !Objects.equals(value.dailyLimit, value.dailySold);
            items.add(new Item(value.getSalable(),value.price, value.dailyLimit,value.dailySold,available,null,value));
        }
        return items;
    }

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

    TheStardropSaloonStuff(IngredientsSupplier product, Integer price, Integer dailyLimit) {
        this.ingredientsSupplier = product;
        this.price = price;
        this.dailyLimit = dailyLimit;
    }

    public static Response purchase(String name, Integer count) {
        TheStardropSaloonStuff salable = null;
        for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
            if (value.getSalable() instanceof Recipe) continue;
            if (value.getSalable().getName().equalsIgnoreCase(name)) {
                salable = value;
            }
        }
        if (salable != null) {
            if (salable.dailyLimit != -1 && salable.dailyLimit < salable.dailySold + count) {
                return new Response("Not enough in stock");
            }
            Player player = App.getInstance().getCurrentGame().getCurrentPlayer();
            if (!player.getInventory().isInventoryHaveCapacity(salable.getSalable())) {
                return new Response("Not enough space in your backpack.");
            }
            if (player.getAccount().getGolds() < salable.getPrice()) {
                return new Response("Not enough golds");
            }
            player.getAccount().removeGolds(salable.getPrice());
            GameClient.getInstance().updatePlayerGold(player);
            salable.dailySold += count;
            Salable item = null;
            if (salable.getSalable() instanceof MadeProductType)
                item = new MadeProduct((MadeProductType) salable.product);
            if (salable.getSalable() instanceof FoodType) item = new Food((FoodType) salable.product);
            if (item == null) throw new InvalidInputException("Item not found in Star drop Saloon");

            player.getInventory().addProductToBackPack(item, count);
            GameClient.getInstance().updatePlayerAddToInventory(player,item,count);
            return new Response("Bought successfully", true);
        }
        for (TheStardropSaloonStuff value : TheStardropSaloonStuff.values()) {
            if (!(value.getSalable() instanceof Recipe)) continue;
            if (value.getSalable().getName().equals(name)) {
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
        GameClient.getInstance().updatePlayerGold(player);
        salable.dailySold += count;
        player.getCookingRecipes().put((CookingRecipe) salable.product, true);
        return new Response("Bought successfully", true);
    }

    public void resetDailySold() {
        dailySold = 0;
    }

    public void increaseDailySold(int amount){
        dailySold += amount;
    }
}
