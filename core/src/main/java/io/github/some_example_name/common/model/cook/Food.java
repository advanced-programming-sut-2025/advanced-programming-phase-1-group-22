package io.github.some_example_name.common.model.cook;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.common.model.Salable;
@Getter
@Setter
public class Food implements Salable {
    private Integer id;
    private FoodType foodType;
    private Sprite sprite;

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

    @Override
    public Texture getTexture() {
        return foodType.getTexture();
    }

    @Override
    public Sprite getSprite() {
        if (sprite == null) {
            sprite = new Sprite(foodType.getTexture());
        }
        return sprite;
    }

    @Override
    public Food copy() {
        return new Food(foodType);
    }
}
