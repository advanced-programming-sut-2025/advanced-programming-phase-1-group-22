package model.cook;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
@Getter
@Setter
public class Food implements Salable {
    private Integer id;
    private FoodType foodType;

    @Override
    public String getName() {
        return this.foodType.getName();
    }

    @Override
    public int getSellPrice() {
        return foodType.getSellPrice();
    }
}
