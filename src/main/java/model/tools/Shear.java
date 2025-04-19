package model.tools;

import lombok.Getter;

@Getter
public enum Shear implements Tool {
    SHEAR("shear",4, 1000);

    private final String name;
    private final Integer energyUse;
    private final Integer price;

    Shear(String name,Integer energyUse, Integer price) {
        this.name = name;
        this.energyUse = energyUse;
        this.price = price;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return this.name.toLowerCase();
    }
}
