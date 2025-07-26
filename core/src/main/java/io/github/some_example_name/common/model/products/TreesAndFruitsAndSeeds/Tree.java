package io.github.some_example_name.common.model.products.TreesAndFruitsAndSeeds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.some_example_name.common.model.Tuple;
import io.github.some_example_name.common.model.dto.SpriteHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import io.github.some_example_name.common.model.TimeAndDate;
import io.github.some_example_name.common.model.gameSundry.SundryType;
import io.github.some_example_name.common.model.products.HarvestAbleProduct;
import io.github.some_example_name.common.utils.App;

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
    private transient ArrayList<SpriteHolder> sprites = new ArrayList<>();
    private int spriteSituation = 0;

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
        if (startPlanting == null) return level;
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
        if (treeType == null || treeType.getFruit() == null) return 0;
        return treeType.getFruit().getSellPrice();
    }

    @Override
    public Integer getContainingEnergy() {
        if (treeType == null || treeType.getFruit() == null) return 0;
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
    public ArrayList<SpriteHolder> getSprites() {
        if (sprites.isEmpty()) {
            this.sprites.add(new SpriteHolder(new Sprite(treeType.getTextures().get(0))));
            this.sprites.get(0).setOffset(new Tuple<>(-0.5f, 0f));
            this.sprites.get(0).setSize(App.tileWidth * 2, App.tileHeight * 3);
        }
        if (this.isBurn && spriteSituation != -1){
            this.sprites.get(0).setSprite(new Sprite(this.treeType.getBurnTexture()));
            spriteSituation = -1;
            return this.sprites;
        }
        if (this.treeType.getIsForaging()){
            return this.sprites;
        }
        if (canHarvest() && spriteSituation == -2){
            this.sprites.get(0).setSprite(new Sprite(this.treeType.getFruitedTexture()));
            spriteSituation = -2;
            return this.sprites;
        }
        int level = calculateRegrowthLevel();
        if (level == 5 && spriteSituation != level){
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
            this.sprites.get(0).setSprite(new Sprite(trees[season]));
            spriteSituation = level;
            return this.sprites;
        }
        if (spriteSituation != level) {
            this.sprites.get(0).setSprite(new Sprite(this.treeType.getTextures().get(level - 1)));
            spriteSituation = level;
        }
        return this.sprites;
    }

    @Override
    public Tree copy() {
        return new Tree(treeType);
    }
}
