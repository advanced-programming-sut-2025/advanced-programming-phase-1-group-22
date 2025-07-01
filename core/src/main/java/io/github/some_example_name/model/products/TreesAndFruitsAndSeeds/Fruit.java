package io.github.some_example_name.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.products.HarvestAbleProduct;
import io.github.some_example_name.model.products.Harvestable;
import io.github.some_example_name.model.structure.Structure;

@Getter
@Setter
@ToString
public class Fruit implements Salable{
    FruitType fruitType;
    private Texture texture;
    private Sprite sprite;

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
}
