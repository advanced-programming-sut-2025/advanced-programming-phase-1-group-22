package io.github.some_example_name.model.products;

import lombok.Getter;
import lombok.Setter;
import io.github.some_example_name.model.Salable;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.gameSundry.SundryType;
import io.github.some_example_name.model.structure.Structure;

import java.util.List;

@Getter
@Setter
public abstract class HarvestAbleProduct extends Structure implements Salable {
    public abstract String getName();
    public abstract int getSellPrice();
    public abstract Integer getContainingEnergy();
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
    public abstract boolean getBurn();
    public abstract Boolean getAroundSprinkler();
    public abstract void setAroundSprinkler(Boolean aroundSprinkler);
    public abstract Boolean getAroundScareCrow();
    public abstract void setAroundScareCrow(Boolean aroundScareCrow);
    public abstract  Boolean getInGreenHouse();
    public abstract void setInGreenHouse(Boolean inGreenHouse);
    public abstract List<SundryType> getFertilizes();
}
