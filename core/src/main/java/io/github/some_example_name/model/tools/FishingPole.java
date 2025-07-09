package io.github.some_example_name.model.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import io.github.some_example_name.model.records.Response;
import io.github.some_example_name.utils.GameAsset;
import lombok.Getter;
import io.github.some_example_name.model.animal.Fish;
import io.github.some_example_name.model.animal.FishType;
import io.github.some_example_name.model.products.AnimalProduct;
import io.github.some_example_name.model.products.ProductQuality;
import io.github.some_example_name.model.relations.Player;
import io.github.some_example_name.model.Tile;
import io.github.some_example_name.model.abilitiy.Ability;
import io.github.some_example_name.utils.App;

import java.util.List;
import java.util.Random;

@Getter
public enum FishingPole implements Tool {
    TRAINING("training fishingPole", 25, 0, 0, 8, 0.1,
        new Sprite(GameAsset.TRAINING_ROD), GameAsset.TRAINING_ROD),
    BAMBOO("bamboo fishingPole", 500, 1, 0, 8, 0.5,
        new Sprite(GameAsset.BAMBOO_POLE), GameAsset.BAMBOO_POLE),
    FIBER_GLASS("fiber glass fishingPole", 1_8000, 2, 2, 6, 0.9,
        new Sprite(GameAsset.FIBERGLASS_ROD), GameAsset.FIBERGLASS_ROD),
    IRIDIUM("iridium fishingPole", 7_500, 3, 4, 4, 1.2,
        new Sprite(GameAsset.IRIDIUM_ROD), GameAsset.IRIDIUM_ROD);

    private final String name;
    private final Integer price;
    private final Integer level;
    private final Integer abilityLevel;
    private final Integer energyCost;
    private final Double qualityPercent;
    private final Texture texture;
    private final Sprite sprite;

    FishingPole(String name, Integer price, Integer level, Integer abilityLevel, Integer energyCost, Double qualityPercent, Sprite sprite, Texture texture) {
        this.name = name;
        this.price = price;
        this.level = level;
        this.abilityLevel = abilityLevel;
        this.energyCost = energyCost;
        this.qualityPercent = qualityPercent;
        this.sprite = sprite;
        this.sprite.setSize(App.tileWidth, App.tileHeight);
        this.texture = texture;
    }

    public void finishing() {

    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }

    @Override
    public int getSellPrice() {
        return price;
    }

    @Override
    public Tool getToolByLevel(int level) {
        for (FishingPole value : FishingPole.values()) {
            if (value.getLevel() == level) {
                return value;
            }
        }
        return null;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public int getEnergy(Player player) {
        int minus = 0;
        if (player.getAbilityLevel(Ability.FISHING) == 4) {
            minus += 1;
        }
        if (player.getBuffAbility() != null && player.getBuffAbility().equals(Ability.FISHING)) {
            minus += 1;
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - minus);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        return "";
    }

    public boolean canFish(Player player, Fish fish) {
        if (this.getEnergy(player) > player.getEnergy()) {
            return false;
        }
        return player.getInventory().isInventoryHaveCapacity(fish);
    }

    public Fish fishing(Player player) {
        Double quality = generateQuality(player);
        FishType fishType = generateFishType(player);
        Fish fish = new Fish(fishType);
        fish.setProductQuality(ProductQuality.getQualityByDouble(quality));
        return fish;
    }

    public int generateNumberOfFish(Player player) {
        Random random = new Random();
        double R = random.nextDouble(0, 1);
        int skill = player.getAbilityLevel(Ability.FISHING);
        Double M = App.getInstance().getCurrentGame().getVillage().getWeather().getFishingCoefficient();
        return (int) Math.ceil(R * M * (skill + 2));
    }

    private Double generateQuality(Player player) {
        Random random = new Random();
        double R = random.nextDouble(0, 1);
        int skill = player.getAbilityLevel(Ability.FISHING);
        Double M = App.getInstance().getCurrentGame().getVillage().getWeather().getFishingCoefficient();
        return (R * (skill + 2) * this.qualityPercent) / (7 - M);
    }

    private FishType generateFishType(Player player) {
        Random random = new Random();
        int bound = FishType.values().length - 1;
        if (player.getAbilityLevel(Ability.FISHING) < 4) {
            bound = 13;
        }
        FishType fishType = FishType.values()[random.nextInt(0, bound)];
        while (!fishType.getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())) {
            fishType = FishType.values()[random.nextInt(0, bound)];
        }
        return fishType;
    }

    @Override
    public Integer getContainingEnergy() {
        return 0;
    }
}
