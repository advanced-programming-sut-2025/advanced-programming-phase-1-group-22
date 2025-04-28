package model.receipe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Fridge;
import model.Salable;
import model.cook.FoodType;
import utils.App;

@Getter
@Setter
public class CookingRecipe implements Recipe {
    //TODO Adding other recipes on the due time available

    private String name;
    private Integer id;
    private String description;
    private Integer price;
    private FoodType foodType;

    public CookingRecipe(String name, String description, Integer price, FoodType foodType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.foodType = foodType;
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
        String res = foodType.getName() + ":\n\n" + foodType.getBuff().toString() + "\n\nIngredients:\n";
        res += foodType.getProductsString();
        res += "\nEnergy: " + foodType.getEnergy() + "$\n";
        res += "\nPrice: " + foodType.getSellPrice() + "$\n";
        res += foodType.isValidIngredient(fridge) + "\n";
        return res;
    }
}
