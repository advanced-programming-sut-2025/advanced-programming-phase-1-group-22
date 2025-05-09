package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.TimeAndDate;
import model.products.HarvestAbleProduct;
import model.products.Harvestable;
import model.structure.Structure;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Fruit implements Salable, Serializable {
    FruitType fruitType;

    public Fruit() {
    }

    public Fruit(FruitType fruitType) {
        this.fruitType = fruitType;
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