package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.TimeAndDate;
import model.gameSundry.SundryType;
import model.products.HarvestAbleProduct;
import utils.App;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class Tree extends HarvestAbleProduct {
    private TreeType treeType;
    private TimeAndDate startPlanting;
    private TimeAndDate lastHarvest;
    private Boolean isWaterToday = false;
    private Boolean isFertilized = false;
    private Boolean attackByCrow = false;
    private Boolean isAroundSprinkler = false;
    private Boolean isAroundScareCrow = false;
    private Boolean isBurn = false;
    private Boolean isBroken = false;
    private Boolean isInGreenHouse = false;
    private List<SundryType> fertilizes = new ArrayList<>();
    private Integer numberOfWithoutWaterDays = 0;
    private Integer numberOfStages = 0;

    public Tree(TreeType treeType) {
        this.treeType = treeType;
        if (treeType.getHarvestStages() != null) {
            numberOfStages = treeType.getHarvestStages().size();
        } else {
            numberOfStages = 0;
        }
    }

    public void burn() {
        this.isBurn = true;
    }

    public void breakTree() {
        this.isBroken = true;
    }

    public int calculateRegrowthLevel() {
        int level = 1;
        TimeAndDate now = App.getInstance().getCurrentGame().getTimeAndDate();
        int spendDays = now.getDay() - this.startPlanting.getDay();
        if (this.treeType.getHarvestStages() != null) {
            int sum = 0;
            for (Integer harvestStage : this.treeType.getHarvestStages()) {
                sum += harvestStage;
                if (spendDays >= sum) {
                    level += 1;
                }
            }
        }
        return level;
    }

    public int calculateDaysAfterPlanting() {
        return App.getInstance().getCurrentGame().getTimeAndDate().getDay() - this.startPlanting.getDay();
    }

    public int calculateDaysAfterLastHarvest() {
        return App.getInstance().getCurrentGame().getTimeAndDate().getDay() - this.lastHarvest.getDay();
    }

    public int calculateTotalHarvestTime() {
        if (this.treeType.getHarvestStages() != null) {
            int total = 0;
            for (Integer harvestStage : this.treeType.getHarvestStages()) {
                total += harvestStage;
            }
            return total;
        }
        return 0;
    }

    public boolean canHarvest() {
        int totalHarvestTime = calculateTotalHarvestTime();
        int regrowthTime = this.treeType.getHarvestCycle();
        if (this.fertilizes.contains(SundryType.SPEED_GROW)) {
            totalHarvestTime = Math.max(0, totalHarvestTime - 1);
            regrowthTime = Math.max(0, regrowthTime - 1);
        }
        if (attackByCrow) {
            return false;
        }
        if (calculateDaysAfterPlanting() >= totalHarvestTime) {
            if (lastHarvest == null) {
                return true;
            }
            return calculateDaysAfterLastHarvest() >= regrowthTime;
        }
        return false;
    }

    @Override
    public boolean getIsOneTime() {
        return false;
    }

    @Override
    public void setLastHarvest(TimeAndDate lastHarvest) {
        this.lastHarvest = lastHarvest;
    }

    @Override
    public void setFertilized(Boolean fertilized) {
        this.isFertilized = fertilized;
    }

    public int remainDaysUntilCanHarvest() {
        if (this.treeType.getIsForaging()) {
            return 0;
        } else {
            if (calculateDaysAfterPlanting() >= calculateTotalHarvestTime()) {
                if (lastHarvest == null) {
                    return 0;
                }
                return Math.max(0, this.treeType.getHarvestCycle() - calculateDaysAfterLastHarvest());
            }
            return calculateTotalHarvestTime() - calculateDaysAfterPlanting();
        }
    }

    @Override
    public void setWaterToday(Boolean waterToday) {
        this.isWaterToday = waterToday;
    }

    @Override
    public String getName() {
        return treeType.getName();
    }

    @Override
    public int getSellPrice() {
        return treeType.getFruit().getSellPrice();
    }

    @Override
    public int getContainingEnergy() {
        return treeType.getFruit().getFruitEnergy();
    }

    @Override
    public int getNumberOfWithoutWaterDays() {
        return this.numberOfWithoutWaterDays;
    }

    @Override
    public boolean getIsFertilized() {
        return this.isFertilized;
    }

    @Override
    public boolean getIsWaterToday() {
        return this.isWaterToday;
    }

    @Override
    public boolean getBurn() {
        return isBurn;
    }

    public Boolean getAroundSprinkler() {
        return isAroundSprinkler;
    }

    public void setAroundSprinkler(Boolean aroundSprinkler) {
        isAroundSprinkler = aroundSprinkler;
        isWaterToday = true;
    }

    public Boolean getAroundScareCrow() {
        return isAroundScareCrow;
    }

    public void setAroundScareCrow(Boolean aroundScareCrow) {
        isAroundScareCrow = aroundScareCrow;
    }

    @Override
    public Boolean getInGreenHouse() {
        return isInGreenHouse;
    }

    @Override
    public void setInGreenHouse(Boolean inGreenHouse) {
        isInGreenHouse = inGreenHouse;
    }

    @Override
    public List<SundryType> getFertilizes() {
        return fertilizes;
    }
}