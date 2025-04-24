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
@ToString
public class CookingRecipe implements Recipe {

    private String name;
    private Integer id;
    private String description;
    private Integer price;
    private FoodType foodType; //TODO

    public CookingRecipe(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
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
