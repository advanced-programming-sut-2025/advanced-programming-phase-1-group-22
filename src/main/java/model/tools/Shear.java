package model.tools;

import lombok.Getter;

@Getter
public enum Shear implements Tool {
    SHEAR(4, 1000);
    private final Integer energyUse;
    private final Integer price;

    Shear(Integer energyUse, Integer price) {
        this.energyUse = energyUse;
        this.price = price;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }
}
