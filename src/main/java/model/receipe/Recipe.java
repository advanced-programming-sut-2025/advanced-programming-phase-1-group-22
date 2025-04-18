package model.receipe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;

@Getter
@Setter
@ToString
public class Recipe implements Salable {
    private Integer id;
    private String description;
    private Integer price;

    public Recipe(String description, Integer price) {
        this.description = description;
        this.price = price;
    }

    public void addCookingRecipe() {

    }

    public void addCraftingRecipe() {

    }
}
