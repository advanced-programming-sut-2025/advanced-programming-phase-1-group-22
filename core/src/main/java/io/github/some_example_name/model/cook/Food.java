package io.github.some_example_name.model.cook;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.Salable;
@Getter
@Setter
public class Food implements Salable {
    private Integer id;
    private FoodType foodType;

    public Food(FoodType foodType) {
        this.foodType = foodType;
    }

    @Override
    public String getName() {
        return this.foodType.getName();
    }

    @Override
    public int getSellPrice() {
        return foodType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return foodType.getContainingEnergy();}
}
