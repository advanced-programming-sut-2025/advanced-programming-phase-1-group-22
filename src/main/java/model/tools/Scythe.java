package model.tools;

import lombok.Getter;

@Getter
public class Scythe implements Tool {
    private final Integer energyUse = 2;

    public Integer getEnergyUse() {
        return energyUse;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }

    @Override
    public String getName() {
        return "scythe";
    }

    @Override
    public int getSellPrice() {
        return 0;
    }
}
