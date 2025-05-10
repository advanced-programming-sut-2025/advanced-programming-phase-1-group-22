package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.Salable;
import model.TimeAndDate;
import model.products.HarvestAbleProduct;
import model.products.Harvestable;
import model.structure.Structure;

@Getter
@Setter
@ToString
public class Fruit implements Salable{
    FruitType fruitType;

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