package model.tools;

import lombok.Getter;

@Getter
public enum FishingPole implements Tool {
    TRAINING(25,0,8,0.1),
    BAMBOO(500,0,8,0.5),
    FIBER_GLASS(1_800,2,6,0.9),
    IRIDIUM(7_500,4,4,1.2);

    private final Integer price;
    private final Integer abilityLevel;
    private final Integer energyCost;
    private final Double qualityPercent;

    FishingPole(Integer price, Integer abilityLevel, Integer energyCost, Double qualityPercent) {
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
}
