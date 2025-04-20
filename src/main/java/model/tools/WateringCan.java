package model.tools;

import lombok.Getter;

@Getter

public enum WateringCan implements Tool {
    NORMAL("normal wateringCan",0,40,5),
    CUPPER("cupper wateringCan",1,55,4),
    IRON("iron wateringCan",2,70,3),
    GOLD("gold wateringCan",3,85,2),
    IRIDIUM("iridium wateringCan",4,100,1);

    private final String name;
    private final Integer level;
    private final Integer capacity;
    private final Integer energyUse;
     WateringCan(String name,Integer level, Integer capacity, Integer energyUse) {
        this.name = name;
         this.level = level;
        this.capacity = capacity;
        this.energyUse = energyUse;
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
        return 0;
    }
}
