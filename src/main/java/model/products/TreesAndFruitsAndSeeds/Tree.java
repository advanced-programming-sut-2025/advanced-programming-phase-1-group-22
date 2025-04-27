package model.products.TreesAndFruitsAndSeeds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import model.TimeAndDate;
import model.products.HarvestAbleProduct;
import utils.App;

@Getter
@Setter
@ToString
public class Tree extends HarvestAbleProduct {
    private TreeType treeType;
    private TimeAndDate startPlanting;
    private TimeAndDate lastHarvest;
    private Boolean isWaterToday;
    private Boolean isFertilized;
    private Boolean isBurn;
    private Boolean isBroken;
    private Integer numberOfWithoutWaterDays;
    private int numberOfStages;

    public Tree(TreeType treeType) {
        this.treeType = treeType;
        if (treeType.getHarvestStages() != null){
            numberOfStages = treeType.getHarvestStages().size();
        }
        else {
            numberOfStages = 0;
        }
    }

    public void burn() {
        this.isBurn = true;
    }

    public void breakTree(){
        this.isBroken = true;
    }

    public int calculateRegrowthLevel(){
        int level = 1;
        TimeAndDate now = App.getInstance().getCurrentGame().getTimeAndDate();
        int spendDays = now.getDay() - this.startPlanting.getDay();
        if (this.treeType.getHarvestStages() != null){
            int sum = 0;
            for (Integer harvestStage : this.treeType.getHarvestStages()) {
                sum += harvestStage;
                if (spendDays >= sum){
                    level += 1;
                }
            }
        }
        return level;
    }

    public int calculateDaysAfterPlanting(){
        return App.getInstance().getCurrentGame().getTimeAndDate().getDay() - this.startPlanting.getDay();
    }

    public int calculateDaysAfterLastHarvest(){
        return App.getInstance().getCurrentGame().getTimeAndDate().getDay() - this.lastHarvest.getDay();
    }

    public int calculateTotalHarvestTime(){
        if (this.treeType.getHarvestStages() != null){
            int total = 0;
            for (Integer harvestStage : this.treeType.getHarvestStages()) {
                total += harvestStage;
            }
            return total;
        }
        return 0;
    }

    public boolean canHarvest(){
        if (this.treeType.getIsForaging()){
            return false;
        }
        else {
            if (calculateDaysAfterPlanting() >= calculateTotalHarvestTime()){
                if (lastHarvest == null){
                    return true;
                }
                return calculateDaysAfterLastHarvest() >= this.treeType.getHarvestCycle();
            }
            return false;
        }
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

    public int remainDaysUntilCanHarvest(){
        if (this.treeType.getIsForaging()){
            return 0;
        }
        else {
            if (calculateDaysAfterPlanting() >= calculateTotalHarvestTime()){
                if (lastHarvest == null){
                    return 0;
                }
                return Math.max(0,this.treeType.getHarvestCycle() - calculateDaysAfterLastHarvest());
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
    public boolean getIsFertilized(){
        return this.isFertilized;
    }

    @Override
    public boolean getIsWaterToday(){
        return this.isWaterToday;
    }

    @Override
    public boolean getBurn() {
        return isBurn;
    }
}