package model.products;

import lombok.Getter;
import lombok.Setter;
import model.Salable;
import model.TimeAndDate;
import model.structure.Structure;

@Getter
@Setter
public abstract class HarvestAbleProduct extends Structure implements Salable {
    public abstract String getName();
    public abstract int getSellPrice();
    public abstract int getContainingEnergy();
    public abstract void setStartPlanting(TimeAndDate startPlanting);
    public abstract int calculateRegrowthLevel();
    public abstract int remainDaysUntilCanHarvest();
    public abstract boolean getIsWaterToday();
    public abstract void setWaterToday(Boolean waterToday);
    public abstract boolean getIsFertilized();
    public abstract boolean canHarvest();
    public abstract boolean getIsOneTime();
    public abstract void setLastHarvest(TimeAndDate lastHarvest);
    public abstract void setFertilized(Boolean fertilized);
    public abstract void setNumberOfWithoutWaterDays(Integer numberOfWithoutWaterDays);
    public abstract int getNumberOfWithoutWaterDays();
}