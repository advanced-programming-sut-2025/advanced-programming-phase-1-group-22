package model.receipe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.craft.CraftType;
import utils.App;

@Getter
@Setter
public class CraftingRecipe implements Recipe {
    //TODO Adding other recipes on the due time available

    private String name;
    private Integer id;
    private String description;
    private Integer price;
    private CraftType craftType;

    public CraftingRecipe(String name, String description, Integer price, CraftType craftType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.craftType = craftType;
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
        String res = craftType.getName() + ":\n\n" + craftType.getDescription() + "\n\nIngredients:\n";
        res += craftType.getProductsString();
        res += "\nPrice: " + craftType.getSellPrice() + "$\n";
        res += craftType.isCraftingPossible(App.getInstance().getCurrentGame().getCurrentPlayer()) + "\n";
        return res;
    }
}
