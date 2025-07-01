package io.github.some_example_name.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.model.TimeAndDate;
import io.github.some_example_name.model.gameSundry.SundryType;
import io.github.some_example_name.model.products.HarvestAbleProduct;
import io.github.some_example_name.utils.App;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
    private Sprite sprite;

    public Tree(TreeType treeType) {
        this.treeType = treeType;
        if (treeType.getHarvestStages() != null) {
            numberOfStages = treeType.getHarvestStages().size();
        } else {
            numberOfStages = 0;
        }
        this.sprite = new Sprite(treeType.getTextures().get(0));
        this.sprite.setSize(App.tileWidth * 2,App.tileHeight * 3);
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
        int spendDays = now.getTotalDays() - this.startPlanting.getDay();
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
        return App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays() - this.startPlanting.getDay();
    }

    public int calculateDaysAfterLastHarvest() {
        return App.getInstance().getCurrentGame().getTimeAndDate().getTotalDays() - this.lastHarvest.getDay();
    }

    public boolean canHarvest() {
        int totalHarvestTime = 28;
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
            if (calculateDaysAfterPlanting() >= 28) {
                if (lastHarvest == null) {
                    return 0;
                }
                return Math.max(0, this.treeType.getHarvestCycle() - calculateDaysAfterLastHarvest());
            }
            return 28 - calculateDaysAfterPlanting();
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
    public Integer getContainingEnergy() {
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

    @Override
    public Sprite getSprite() {
        if (this.isBurn){
            this.sprite = new Sprite(this.treeType.getBurnTexture());
            this.sprite.setSize(App.tileWidth * 2,App.tileHeight * 3);
            return this.sprite;
        }
        if (this.treeType.getIsForaging()){
            return this.sprite;
        }
        if (canHarvest()){
            this.sprite = new Sprite(this.treeType.getFruitedTexture());
            this.sprite.setSize(App.tileWidth * 2,App.tileHeight * 3);
            return this.sprite;
        }
        int level = calculateRegrowthLevel();
        if (level == 5){
            Texture treeSheet = treeType.getTextures().get(4);
            TextureRegion[] trees = new TextureRegion[4];
            for (int i = 0; i < 4; i++) {
                trees[i] = new TextureRegion(treeSheet, i * (treeSheet.getWidth() / 4),
                    0, treeSheet.getWidth() / 4, treeSheet.getHeight());
            }
            int season = 0;
            switch (App.getInstance().getCurrentGame().getTimeAndDate().getSeason()){
                case SPRING -> season = 0;
                case SUMMER -> season = 1;
                case FALL -> season = 2;
                case WINTER -> season = 3;
            }
            this.sprite = new Sprite(trees[season]);
            this.sprite.setSize(App.tileWidth * 2,App.tileHeight * 3);
            return this.sprite;
        }
        this.sprite = new Sprite(this.treeType.getTextures().get(level - 1));
        this.sprite.setSize(App.tileWidth * 2,App.tileHeight * 3);
        return this.sprite;
    }
}
