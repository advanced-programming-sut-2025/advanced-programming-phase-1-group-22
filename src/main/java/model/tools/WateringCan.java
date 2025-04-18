package model.tools;

import lombok.Getter;

@Getter

public enum WateringCan implements Tool {
    NORMAL(0,40,5),CUPPER(1,55,4),IRON(2,70,3),
    GOLD(3,85,2),IRIDIUM(4,100,1);
    private final Integer level;
    private final Integer capacity;
    private final Integer energyUse;
     WateringCan(Integer level, Integer capacity, Integer energyUse) {
        this.level = level;
        this.capacity = capacity;
        this.energyUse = energyUse;
    }

    @Override
    public void addToolEfficiency(double efficiency) {

    }
}
