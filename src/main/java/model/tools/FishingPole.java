package model.tools;

import lombok.Getter;
import model.relations.Player;
import model.Tile;
import model.abilitiy.Ability;

@Getter
public enum FishingPole implements Tool {
    TRAINING("training fishingPole",25,0,8,0.1),
    BAMBOO("bamboo fishingPole",500,0,8,0.5),
    FIBER_GLASS("fiber glass fishingPole",1_800,2,6,0.9),
    IRIDIUM("iridium fishingPole",7_500,4,4,1.2);

    private final String name;
    private final Integer price;
    private final Integer abilityLevel;
    private final Integer energyCost;
    private final Double qualityPercent;

    FishingPole(String name,Integer price, Integer abilityLevel, Integer energyCost, Double qualityPercent) {
        this.name = name;
        this.price = price;
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
        return null;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getEnergy(Player player) {
        if (player.getAbilityLevel(Ability.FISHING) == 4){
            return energyCost - 1;
        }
        return energyCost;
    }

    @Override
    public String useTool(Player player, Tile tile) {
        boolean success = false;



        if (success){
            player.upgradeAbility(Ability.FISHING);
            return "you successfully use this tool";
        }
        player.changeEnergy(-this.getEnergy(player));
        return "you use this tool in a wrong way";
    }
}
