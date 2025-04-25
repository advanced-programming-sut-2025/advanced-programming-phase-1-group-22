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
public class Fruit extends HarvestAbleProduct{
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
    public int getContainingEnergy() {
        return fruitType.getFruitEnergy();
    }

    @Override
    public void setStartPlanting(TimeAndDate startPlanting) {

    }

    @Override
    public int calculateRegrowthLevel() {
        return 0;
    }

    @Override
    public int remainDaysUntilCanHarvest() {
        return 0;
    }

    @Override
    public boolean getIsWaterToday() {
        return false;
    }

    @Override
    public void setWaterToday(Boolean waterToday) {

    }

    @Override
    public boolean getIsFertilized() {
        return false;
    }

    @Override
    public boolean canHarvest() {
        return false;
    }

    @Override
    public boolean getIsOneTime() {
        return false;
    }

    @Override
    public void setLastHarvest(TimeAndDate lastHarvest) {

    }

    @Override
    public void setFertilized(Boolean fertilized) {

    }

    @Override
    public void setNumberOfWithoutWaterDays(Integer numberOfWithoutWaterDays) {

    }

    @Override
    public int getNumberOfWithoutWaterDays() {
        return 0;
    }
}
