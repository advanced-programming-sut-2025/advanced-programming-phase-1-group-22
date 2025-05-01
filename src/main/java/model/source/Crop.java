package model.source;

import lombok.Getter;
import lombok.Setter;
import model.TimeAndDate;
import model.gameSundry.SundryType;
import model.products.HarvestAbleProduct;
import utils.App;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class Crop extends HarvestAbleProduct implements Source {
    private CropType cropType;
    private TimeAndDate startPlanting;
    private TimeAndDate lastHarvest;
    private Boolean isAroundSprinkler;
    private Boolean isAroundScareCrow;
    private Boolean isWaterToday;
    private Boolean isFertilized;
    private Boolean isBurn;
    private Boolean isGiant;
    private Boolean isInGreenHouse;
    private List<SundryType> fertilizes = new ArrayList<>();
    private Integer numberOfWithoutWaterDays;
    private int numberOfStages;

    public Crop(CropType cropType) {
        this.cropType = cropType;
        if (cropType.getHarvestStages() != null){
            numberOfStages = cropType.getHarvestStages().size();
        }
        else {
            numberOfStages = 0;
        }
    }

    @Override
    public String getName(){
        return this.cropType.getName();
    }

    @Override
    public int getSellPrice() {
        return cropType.getSellPrice();
    }

    @Override
    public int getContainingEnergy() {
        return cropType.getEnergy();
    }

    public void burn() {
        this.isBurn = true;
    }

    public int calculateRegrowthLevel(){
        int level = 1;
        TimeAndDate now = App.getInstance().getCurrentGame().getTimeAndDate();
        int spendDays = now.getDay() - this.startPlanting.getDay();
        if (this.cropType.getHarvestStages() != null){
            int sum = 0;
            for (Integer harvestStage : this.cropType.getHarvestStages()) {
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
        if (this.cropType.getHarvestStages() != null){
            int total = 0;
            for (Integer harvestStage : this.cropType.getHarvestStages()) {
                total += harvestStage;
            }
            return total;
        }
        return 0;
    }

    public boolean canHarvest(){
        int totalHarvestTime = calculateTotalHarvestTime();
        int regrowthTime = this.cropType.getRegrowthTime();
        if (this.fertilizes.contains(SundryType.SPEED_GROW)){
            totalHarvestTime = Math.max(0,totalHarvestTime - 1);
            regrowthTime = Math.max(0,regrowthTime - 1);
        }
        if (this.cropType.isForaging()){
            return true;
        }
        else {
            if (calculateDaysAfterPlanting() >= totalHarvestTime){
                if (lastHarvest == null){
                    return true;
                }
				return calculateDaysAfterLastHarvest() >= regrowthTime;
			}
            return false;
        }
    }

    @Override
    public boolean getIsOneTime() {
        return this.cropType.isOneTime();
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
        if (this.cropType.isForaging()){
            return 0;
        }
        else {
            if (calculateDaysAfterPlanting() >= calculateTotalHarvestTime()){
                if (lastHarvest == null){
                    return 0;
                }
                return Math.max(0,this.cropType.getRegrowthTime() - calculateDaysAfterLastHarvest());
            }
            return calculateTotalHarvestTime() - calculateDaysAfterPlanting();
        }
    }

    @Override
    public void setWaterToday(Boolean waterToday) {
        this.isWaterToday = waterToday;
    }

    @Override
    public void setNumberOfWithoutWaterDays(Integer numberOfWithoutWaterDays) {
        this.numberOfWithoutWaterDays = numberOfWithoutWaterDays;
    }

    @Override
    public void setStartPlanting(TimeAndDate startPlanting) {
        this.startPlanting = startPlanting;
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

    @Override
    public Boolean getAroundSprinkler() {
        return isAroundSprinkler;
    }

    @Override
    public void setAroundSprinkler(Boolean aroundSprinkler) {
        isAroundSprinkler = aroundSprinkler;
        isWaterToday = true;
    }

    @Override
    public Boolean getAroundScareCrow() {
        return isAroundScareCrow;
    }

    @Override
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