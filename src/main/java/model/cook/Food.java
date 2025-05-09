package model.cook;

import lombok.Getter;
import lombok.Setter;
import model.Salable;

import java.io.Serializable;

@Getter
@Setter
public class Food implements Salable, Serializable {
    private Integer id;
    private FoodType foodType;

    public Food(FoodType foodType) {
        this.foodType = foodType;
    }

    public Food() {
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
