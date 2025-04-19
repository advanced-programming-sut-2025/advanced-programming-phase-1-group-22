package model.tools;

import lombok.Getter;

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
}
