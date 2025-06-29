package io.github.some_example_name.model.source;

import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.gameSundry.SundryType;
import io.github.some_example_name.model.products.HarvestAbleProduct;
import io.github.some_example_name.utils.App;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class Crop extends HarvestAbleProduct implements Source {
	private CropType cropType;
	private TimeAndDate startPlanting;
	private TimeAndDate lastHarvest;
	private Boolean isAroundSprinkler = false;
	private Boolean isAroundScareCrow = false;
	private Boolean isWaterToday = false;
	private Boolean isFertilized = false;
	private Boolean isBurn = false;
	private Boolean isGiant = false;
	private Boolean isInGreenHouse = false;
	private List<SundryType> fertilizes = new ArrayList<>();
	private Integer numberOfWithoutWaterDays = 0;
	private Integer numberOfStages = 0;
    private Sprite sprite;

	public Crop(CropType cropType) {
		this.cropType = cropType;
		if (cropType.getHarvestStages()!=null) {
			numberOfStages = cropType.getHarvestStages().size();
		} else {
			numberOfStages = 0;
		}
        this.sprite = new Sprite(cropType.getTextures().get(0));
        this.sprite.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
	}

	@Override
	public String getName() {
		return this.cropType.getName();
	}

	@Override
	public int getSellPrice() {
		return cropType.getSellPrice();
	}

	@Override
	public Integer getContainingEnergy() {
		return cropType.getEnergy();
	}

	public void burn() {
		this.isBurn = true;
	}

	public int calculateRegrowthLevel() {
		int level = 1;
		TimeAndDate now = App.getInstance().getCurrentGame().getTimeAndDate();
		int spendDays = now.getTotalDays() - this.startPlanting.getDay();
		if (this.cropType.getHarvestStages()!=null) {
			int sum = 0;
			for (Integer harvestStage : this.cropType.getHarvestStages()) {
				sum += harvestStage;
				if (spendDays >= sum) {
					level += 1;
				}
			}
		}
		return level;
	}

	public int calculateDaysAfterPlanting() {
		return App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays() - this.startPlanting.getDay();
	}

	public int calculateDaysAfterLastHarvest() {
		return App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays() - this.lastHarvest.getDay();
	}

	public int calculateTotalHarvestTime() {
		if (this.cropType.getHarvestStages()!=null) {
			int total = 0;
			for (Integer harvestStage : this.cropType.getHarvestStages()) {
				total += harvestStage;
			}
			return total;
		}
		return 0;
	}

	public boolean canHarvest() {
		int totalHarvestTime = calculateTotalHarvestTime();
		int regrowthTime = this.cropType.getRegrowthTime();
		if (this.fertilizes.contains(SundryType.SPEED_GROW)) {
			totalHarvestTime = Math.max(0, totalHarvestTime - 1);
			regrowthTime = Math.max(0, regrowthTime - 1);
		}
		if (this.cropType.isForaging()) {
			return true;
		} else {
			if (calculateDaysAfterPlanting() >= totalHarvestTime) {
				if (lastHarvest==null) {
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

	public int remainDaysUntilCanHarvest() {
		if (this.cropType.isForaging()) {
			return 0;
		} else {
			if (calculateDaysAfterPlanting() >= calculateTotalHarvestTime()) {
				if (lastHarvest==null) {
					return 0;
				}
				return Math.max(0, this.cropType.getRegrowthTime() - calculateDaysAfterLastHarvest());
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

    @Override
    public Sprite getSprite() {
        if (this.cropType.isForaging()) {
            return this.sprite;
        }
        int level = calculateRegrowthLevel();
        this.sprite = new Sprite(this.cropType.getTextures().get(level - 1));
        this.sprite.setSize(App.tileWidth / 2f, App.tileHeight / 2f);
        return this.sprite;
    }
}
