package io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.Salable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Fruit implements Salable{
    FruitType fruitType;
    private transient Texture texture;
    private transient Sprite sprite;

    public Fruit(FruitType fruitType) {
        this.fruitType = fruitType;
        this.texture = fruitType.getTexture();
        this.sprite = new Sprite(fruitType.getTexture());

    }

    @Override
    public String getName() {
        return fruitType.getName();
    }

    @Override
    public int getSellPrice() {
        return fruitType.getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {return fruitType.getEnergy();}

    @Override
    public Fruit copy() {
        return new Fruit(fruitType);
    }
}
