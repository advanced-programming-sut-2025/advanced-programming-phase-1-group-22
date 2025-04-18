package model.tools;

import lombok.Getter;

@Getter
public enum Pickaxe implements Tool {
    NORMAL(0, 5), CUPPER(1, 4), IRON(2, 3),
    GOLD(3, 2), IRIDIUM(4, 1);
    private final int level;
    private final int energyCost;

    Pickaxe(int level1, int energyUse1) {
        this.level = level1;
        this.energyCost = energyUse1;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }
}
