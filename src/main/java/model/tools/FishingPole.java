package model.tools;

import lombok.Getter;
import model.animal.Fish;
import model.animal.FishType;
import model.products.AnimalProduct;
import model.products.ProductQuality;
import model.relations.Player;
import model.Tile;
import model.abilitiy.Ability;
import utils.App;

import java.util.List;
import java.util.Random;

@Getter
public enum FishingPole implements Tool {
    TRAINING("training fishingPole",25,0,0,8,0.1),
    BAMBOO("bamboo fishingPole",500,1,0,8,0.5),
    FIBER_GLASS("fiber glass fishingPole",1_8000,2,2,6,0.9),
    IRIDIUM("iridium fishingPole",7_500,3,4,4,1.2);

    private final String name;
    private final Integer price;
    private final Integer level;
    private final Integer abilityLevel;
    private final Integer energyCost;
    private final Double qualityPercent;

    FishingPole(String name,Integer price,Integer level, Integer abilityLevel, Integer energyCost, Double qualityPercent) {
        this.name = name;
        this.price = price;
        this.level = level;
        this.abilityLevel = abilityLevel;
        this.energyCost = energyCost;
        this.qualityPercent = qualityPercent;
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
            if (value.getLevel() == level){
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
        if (player.getAbilityLevel(Ability.FISHING) == 4){
            return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost - 1);
        }
        return (int) (App.getInstance().getCurrentGame().getWeatherCoefficient() * energyCost);
    }

    @Override
    public String useTool(Player player, Tile tile) {
        int numberOfFish = generateNumberOfFish(player);
        Double quality = generateQuality(player);
        FishType fishType = generateFishType(player);
        Fish fish = new Fish(fishType);
        fish.setProductQuality(ProductQuality.getQualityByDouble(quality));
        if (player.getInventory().isInventoryHaveCapacity(fish)){
            player.getInventory().addProductToBackPack(fish,numberOfFish);
            player.changeEnergy(-this.getEnergy(player));
            return "you got " + numberOfFish + " of " + fishType.getName() +
                    " with quality " + quality +
                    " (" + ProductQuality.getQualityByDouble(quality).toString().toLowerCase() + ")";
        }
        return "your inventory is full so you can not fishing";
    }

    private int generateNumberOfFish(Player player){
        Random random = new Random();
        double R = random.nextDouble(0,1);
        int skill = player.getAbilityLevel(Ability.FISHING);
        Double M = App.getInstance().getCurrentGame().getVillage().getWeather().getFishingCoefficient();
        return (int) Math.ceil(R * M * (skill + 2));
    }

    private Double generateQuality(Player player){
        Random random = new Random();
        double R = random.nextDouble(0,1);
        int skill = player.getAbilityLevel(Ability.FISHING);
        Double M = App.getInstance().getCurrentGame().getVillage().getWeather().getFishingCoefficient();
		return (R * (skill + 2) * this.qualityPercent) / (7 - M);
    }

    private FishType generateFishType(Player player){
        Random random = new Random();
        int bound = FishType.values().length - 1;
        if (player.getAbilityLevel(Ability.FISHING) < 4){
            bound = 13;
        }
        FishType fishType = FishType.values()[random.nextInt(0,bound)];
        while (!fishType.getSeason().equals(App.getInstance().getCurrentGame().getTimeAndDate().getSeason())){
            fishType = FishType.values()[random.nextInt(0,bound)];
        }
        return fishType;
    }

    @Override
    public Integer getContainingEnergy() {return 0;}
}