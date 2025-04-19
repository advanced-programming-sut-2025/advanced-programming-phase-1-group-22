package model.receipe;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;

@Getter
@Setter
@ToString
public class Recipe implements Salable {

    private String name;
    private Integer id;
    private String description;
    private Integer price;

    public Recipe(String name,String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void addCookingRecipe() {

    }

    public void addCraftingRecipe() {

    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
